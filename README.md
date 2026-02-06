# ClinicViewer 📱

App Android (Viewer) dành cho bác sĩ để tra cứu hồ sơ bệnh nhân và theo dõi thống kê phòng khám từ xa. Đồng bộ dữ liệu thời gian thực từ Desktop App qua Supabase.

---

## 🚀 Tính năng nổi bật

### 1. Quản lý bệnh nhân
- **Tra cứu nhanh**: Tìm kiếm bệnh nhân theo tên realtime.
- **Infinite Scroll**: Tự động tải danh sách thông minh, tối ưu hiệu năng.
- **Chi tiết hồ sơ**: Xem lịch sử khám, chẩn đoán và đơn thuốc (tách biệt rõ ràng).
- **Thao tác 1 chạm**: Copy nhanh đơn thuốc hoặc SĐT bệnh nhân.

### 2. Thống kê & Báo cáo (Mới v1.2)
- **Dashboard trực quan**: Theo dõi hoạt động phòng khám.
- **Bộ lọc đa chiều**: Xem thống kê theo Ngày / Tuần / Tháng / Năm.
- **Hiệu năng cao**: Sử dụng **Supabase RPC** để tính toán phía server, tải siêu nhanh.

### 3. Giao diện (Premium UI)
- **Dark Theme**: Giao diện tối hiện đại, bảo vệ mắt.
- **Card-based Layout**: Thiết kế thẻ thông minh, dễ nhìn trên mobile.
- **Responsive**: Tối ưu hiển thị cho nhiều kích thước màn hình.

---

## 🛠 Tech Stack

| Thành phần | Công nghệ |
|------------|----------|
| **Ngôn ngữ** | Kotlin 1.9+ |
| **UI Framework** | Jetpack Compose (Material 3) |
| **Backend** | Supabase (PostgreSQL) |
| **Database Logic** | Supabase RPC (PL/pgSQL Functions) |
| **Networking** | Ktor / Supabase-Kt |
| **Architecture** | MVVM + Repository Pattern |

---

## ⚙️ Cài đặt & Setup

### 1. Yêu cầu
- Android Studio Koala hoặc mới hơn.
- JDK 17+.
- Tài khoản Supabase (đã setup database).

### 2. Cấu hình Supabase RPC
Để tính năng Thống kê hoạt động, cần chạy SQL script tạo hàm RPC:

1. Vào **Supabase Dashboard** → **SQL Editor**.
2. Open file `supabase_stats_functions.sql` trong dự án này.
3. Run toàn bộ script để tạo functions:
   - `get_distinct_months()`
   - `get_stats_by_day()`
   - `get_stats_by_week()`
   - `get_stats_by_month()`
   - `get_stats_by_year()`

### 3. Build App
```bash
# Clean & Build Debug APK
./gradlew.bat clean assembleDebug

# Install lên thiết bị
./gradlew.bat installDebug
```

---

## 📂 Cấu trúc dự án
```text
com.skul9x.clinicviewer
├── data
│   ├── model          # Data Classes (Patient, StatItem...)
│   ├── repository     # Data Logic (PatientRepo, StatsRepo - RPC calls)
│   └── SupabaseClient # Config kết nối
├── ui
│   ├── components     # Reusable UI (Cards, Chips...)
│   ├── screens        # Màn hình chính (Home, Stats, Detail)
│   └── theme          # Colors, Typography (Dark theme)
└── MainActivity.kt    # Entry point & Navigation
```

---

## 📜 Version History
- **v1.2**: RPC Optimization (Server-side aggregated stats).
- **v1.1**: Thêm màn hình Thống kê.
- **v1.0**: Core features (Danh sách, Chi tiết, Tìm kiếm).

---
*Created by Antigravity for PK Ngọc Trường*
