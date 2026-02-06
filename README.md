# ClinicViewer 📱

Ứng dụng Android (Kotlin + Jetpack Compose) dành cho bác sĩ để xem hồ sơ bệnh nhân và đơn thuốc từ xa, đồng bộ hóa thời gian thực với hệ thống qua Supabase.

## 🚀 Tính năng chính
- 📊 **Infinite Scroll**: Tự động tải thêm bệnh nhân khi cuộn xuống cuối danh sách, tối ưu hiệu năng và bộ nhớ.
- 🔍 **Tìm kiếm thông minh**: Tìm kiếm bệnh nhân theo tên realtime, hỗ trợ cả khi đang phân trang.
- 🩺 **Chi tiết bệnh nhân**: 
    - Hiển thị đầy đủ thông tin hành chính, ngày khám (format chuẩn).
    - Tách biệt rõ ràng phần **Chẩn đoán** và **Đơn thuốc** để tránh nhầm lẫn.
- 📋 **Copy nhanh**: Hỗ trợ copy đơn thuốc (chỉ lấy phần thuốc) hoặc SĐT bệnh nhân chỉ với 1 chạm.
- 🌓 **Premium UI**: Giao diện tối hiện đại (Dark Theme), bảng màu Teal/Dark phối hợp hài hòa, tối ưu cho môi trường phòng khám.

## 🛠 Công nghệ sử dụng
- **Core**: Kotlin 1.9+
- **UI Framework**: Jetpack Compose (Material 3)
- **Data Layer**: Supabase SDK (Postgrest + Kotlin Serialization)
- **Networking**: Ktor
- **Architecture**: Repository Pattern

## 🏗 Setup & Build
1. Mở dự án bằng **Android Studio**.
2. Kiểm tra cấu hình kết nối tại `data/SupabaseClient.kt`.
3. Build APK:
   ```bash
   ./gradlew.bat assembleDebug
   ```

---
*Ghi chú: Đây là bản rút gọn (Viewer) dành cho thiết bị di động.*
