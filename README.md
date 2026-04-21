# Snake-Ladder Java Online Game

เกมงู-บันไดพัฒนาโดยใช้ Java + Swing โดยในสถานะปัจจุบันรองรับการเล่นแบบ Offline เป็นหลัก พร้อมระบบ animation, sound, asset loader จาก XML manifest และระบบ logging ลงไฟล์สำหรับใช้งานจริงในงานส่งโปรเจกต์

## สารบัญ
1. [ภาพรวมโปรเจกต์](#ภาพรวมโปรเจกต์)
2. [เทคโนโลยีและสิ่งที่ใช้](#เทคโนโลยีและสิ่งที่ใช้)
3. [โครงสร้างระบบ](#โครงสร้างระบบ)
4. [ลำดับการทำงานของเกม](#ลำดับการทำงานของเกม)
5. [การคำนวณหลักของเกม](#การคำนวณหลักของเกม)
6. [ระบบ Asset และ Sound](#ระบบ-asset-และ-sound)
7. [ระบบ Logging](#ระบบ-logging)
8. [โครงสร้างโฟลเดอร์](#โครงสร้างโฟลเดอร์)
9. [วิธี Build และ Run](#วิธี-build-และ-run)
10. [วิธี Build เป็น JAR สำหรับส่งงาน](#วิธี-build-เป็น-jar-สำหรับส่งงาน)
11. [สถานะฟีเจอร์และข้อสังเกต](#สถานะฟีเจอร์และข้อสังเกต)

## ภาพรวมโปรเจกต์
- เกมใช้สถาปัตยกรรมเชิงแยกหน้าที่: `Domain (Logic)` + `Coordinator` + `UI`
- ฝั่ง Logic คำนวณผลเทิร์น 1 ครั้งแล้วส่งออกเป็น `TurnResult`
- ฝั่ง UI ไม่ไปแก้ state เกมโดยตรง แต่แสดงผลตาม `TurnResult`
- รองรับ animation หลัก 3 ช่วง: ทอยลูกเต๋า, เดินทีละช่อง, warp (งู/บันได)
- มีระบบเสียงและภาพจากไฟล์ manifest เพื่อแยก asset ออกจากโค้ด

## เทคโนโลยีและสิ่งที่ใช้
- ภาษา: `Java`
- GUI: `Java Swing` (`JFrame`, `JPanel`, `JLabel`, `JButton`, `Timer`)
- Audio: `javax.sound.sampled` (`Clip`, `AudioInputStream`)
- Logging: `java.util.logging` พร้อม `FileHandler`
- XML parsing: `DocumentBuilderFactory` (ตั้งค่า harden ป้องกัน XXE)
- Build: ใช้ `javac` + `jar` (ไม่มี build tool ภายนอก)
- Dependencies ภายนอก: `ไม่มี`

## โครงสร้างระบบ

### 1) Entry + Screen Controller
- `Main` เรียก `LogConfig.initialize()` ก่อนเริ่มระบบ
- `DisplayController` รับผิดชอบการสลับหน้าจอทั้งหมด เช่น Menu, Lobby, Game, Finish

### 2) Domain Logic
- `GameLogicalManager` คือ logic กลางของเกม
- `OfflineGameLogicalManager` สืบทอดจาก logic กลางสำหรับโหมด Offline
- `TurnResult` คือ immutable result ของ 1 เทิร์น
- `PlayerData` / `GameStatus` ใช้เก็บ state ผู้เล่น

### 3) UI + Animation
- `GamePanel` วาดบอร์ด/ลูกศร/ปุ่ม และเตรียมตำแหน่งช่อง 1-100
- `OfflineGamePanel` วาดผู้เล่นจริงบนกระดาน
- `OfflineModeCoordinator` เป็นตัว orchestrate event และ flow ต่อเทิร์น
- `OfflineAnimationThread` ทำงาน animation ใน 1 เทิร์น แล้วส่ง completion กลับ coordinator

### 4) Asset + Config
- `AssetManager` โหลด `image/sound` จาก XML manifest
- `GameAsset`, `MenuAsset` เป็นคลัง asset แยกตามหน้าจอ
- ค่าคงที่อยู่ใน `include/config/*` เช่น board map, ขนาด UI, timing animation

## ลำดับการทำงานของเกม

### Startup
1. `Main.main()` เรียก logging setup
2. สร้าง `DisplayController`
3. `DisplayController` โหลด asset และแสดงหน้าเมนู

### เริ่มเกม Offline
1. กด `Offline Mode` จากเมนู
2. `DisplayController.startOfflineMode()` สร้าง logic + coordinator
3. ผู้เล่นเลือก skin ใน Lobby
4. กด Start เพื่อเข้าสู่หน้าเกม

### ต่อหนึ่งเทิร์น
1. ผู้ใช้กดปุ่มทอยลูกเต๋า
2. `OfflineModeCoordinator.onDiceRolled()` เรียก `offlineLogic.playTurn()`
3. Logic คืนค่า `TurnResult`
4. Coordinator สั่งเล่นเสียง + เริ่ม `OfflineAnimationThread`
5. Thread ทำ animation เดิน/warp และส่งผลจบเทิร์น (flag) กลับ
6. Coordinator อ่าน flag แล้ว:
- ถ้ายังไม่จบเกม: ปลดล็อกปุ่มทอย
- ถ้าจบเกม: เล่นเสียงจบเกม + เปลี่ยนไปหน้า Finish

## การคำนวณหลักของเกม

### 1) การเดินตามลูกเต๋า
ใน `GameLogicalManager.playTurn()`:
- ทอยค่า 1-6
- คำนวณตำแหน่งใหม่ `next = current + dice`
- clamp ไม่ให้เกิน 100 ด้วย `Math.min(next, END_INDEX)`

### 2) การ warp (งู/บันได)
- ตรวจจาก map คงที่ใน `GameLogical.LADDERS_UP` และ `GameLogical.SNAKES_DOWN`
- ถ้าตำแหน่งหลังเดินตรงกับจุดเริ่ม warp ให้ย้ายไปตำแหน่งปลายทันที

### 3) เงื่อนไขชนะ
- ถ้าตำแหน่งสุดท้าย `>= 100` ให้ set เป็น 100 และ mark `winner = true`

### 4) การหมุนเทิร์น
- `currentTurnIndex = (currentTurnIndex + 1) % playerList.size()`
- มี guard กันเคสไม่มีผู้เล่นก่อนเริ่มเทิร์น

### 5) การคำนวณตำแหน่งช่องบนกระดาน
ใน `GamePanel.positionSetUp()`:
- กระดาน 10x10 แบบสลับทิศ (serpentine)
- แถวคู่วิ่งซ้ายไปขวา, แถวคี่วิ่งขวาไปซ้าย
- ใช้ `CELL_WIDTH_PX`, `CELL_HEIGHT_PX`, และ offset ปรับตำแหน่งจริงบนภาพพื้นหลัง

### 6) การซ้อนผู้เล่นในช่องเดียวกัน
ใน `OfflineGamePanel`:
- จัดกลุ่มผู้เล่นตาม `visualIndex`
- คำนวณ offset แบบหน้าเต๋า (1-6 จุด) เพื่อไม่ให้ตัวละครทับกันตรงกลางพอดี

## ระบบ Asset และ Sound

### Asset Manifest
- `src/main/resources/manifests/game.xml`
- `src/main/resources/manifests/menu.xml`

รูปแบบ key ที่ได้จาก XML:
- `namespace.key` เช่น `game.background`, `game.dice.1`, `menu.background`

### การโหลดไฟล์
`AssetManager` โหลดจาก:
1. path ภายในโปรเจกต์ (`src/main/resources/...`) ระหว่างพัฒนา
2. classpath resource เมื่อตอนรันจาก JAR

### การเล่นเสียง
- ใช้ `SoundHelper.playSound(pathOrUrl)`
- รองรับทั้ง file path ปกติ และ URL (`file:`, `jar:`)

## ระบบ Logging

### เป้าหมาย
- เก็บ log ลงไฟล์เพื่อใช้งานจริง
- ลด noise ใน console
- ไม่เพิ่ม library ภายนอก

### พฤติกรรมที่ตั้งค่าไว้
- คลาสกลาง: `LogConfig`
- สร้างโฟลเดอร์ `logs` อัตโนมัติถ้ายังไม่มี
- เขียนไฟล์ `logs/app.log`
- `append = true` (ไม่ทับของเดิม)
- formatter: `SimpleFormatter`
- root level: `INFO`
- handler level: `ALL` (รองรับ `FINE` ได้ถ้าอนาคตปรับ root level)
- กัน duplicate logs โดยลบ handler เดิมก่อนเพิ่มใหม่
- หาก init logging ล้มเหลว ระบบยังทำงานต่อ (fail-safe)

## โครงสร้างโฟลเดอร์
```text
Snake-Ladder-Java-Online-Game/
├─ src/
│  ├─ main/java/com/distortionstack/snakeladder/
│  │  ├─ Main.java
│  │  ├─ domain/
│  │  ├─ include/
│  │  └─ ui/
│  └─ main/resources/
│     ├─ assets/
│     └─ manifests/
├─ logs/                  # runtime logs (สร้างอัตโนมัติ)
├─ dist/                  # JAR ที่ build แล้ว
├─ out/                   # output compile ชั่วคราว
└─ README.md
```

## วิธี Build และ Run

### 1) Compile
```bash
mkdir -p out/classes
javac -d out/classes $(find src/main/java -name "*.java")
```

### 2) Copy resource เข้า classpath
```bash
cp -r src/main/resources/* out/classes/
```

### 3) Run จาก class files
```bash
java -cp out/classes com.distortionstack.snakeladder.Main
```

## วิธี Build เป็น JAR สำหรับส่งงาน
```bash
rm -rf out SnakeLadder.jar
mkdir -p out/classes
javac -d out/classes $(find src/main/java -name "*.java")
cp -r src/main/resources/* out/classes/
jar --create --file SnakeLadder.jar --main-class com.distortionstack.snakeladder.Main -C out/classes .
```

คัดลอกเข้าโฟลเดอร์ `dist`:
```bash
mkdir -p dist
cp -f SnakeLadder.jar dist/
```

รัน:
```bash
java -jar dist/SnakeLadder.jar
```

หมายเหตุ:
- ไฟล์ log จะถูกเขียนที่ `logs/app.log` โดยอ้างอิงจากตำแหน่งที่สั่งรันคำสั่ง `java`

## สถานะฟีเจอร์และข้อสังเกต
- โหมดที่พร้อมใช้: `Offline`
- โหมดออนไลน์: มีโครงสร้าง class แล้ว แต่ยังไม่ implement logic/network เต็ม
- เกมเป็น Swing GUI: ต้องรันใน environment ที่มี display (ไม่เหมาะกับ headless server)
- มี debug `System.out` บางจุดที่ยังสามารถ refactor เป็น logger เพิ่มเติมได้ในอนาคต

---

หากต้องการต่อยอด แนะนำลำดับถัดไป:
1. เปลี่ยน `System.out` สำคัญเป็น `Logger`
2. ปิด TODO/stub ที่เหลือ
3. เพิ่ม test แยกสำหรับ `GameLogicalManager` เพื่อ validate กติกาเกมอัตโนมัติ
