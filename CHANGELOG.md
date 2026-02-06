# Changelog - ClinicViewer 📱

## [v1.2 - 2026-02-06] - RPC Optimization
### Changed
- ⚡ **Performance**: Chuyển từ client-side calculations sang Supabase RPC functions
- 📉 **Data Transfer**: Giảm lượng data fetch từ server (chỉ lấy aggregated results)

### Technical
- Tạo 5 RPC functions trên Supabase: `get_distinct_months`, `get_stats_by_day`, `get_stats_by_week`, `get_stats_by_month`, `get_stats_by_year`
- Refactor `StatsRepository.kt` để gọi RPC thay vì fetch all patients
- Thêm response models: `MonthResponse`, `DayStatResponse`, `WeekStatResponse`, `MonthStatResponse`, `YearStatResponse`

---

## [v1.1 - 2026-02-06] - Statistics Feature
### Added
- 📊 **Tab Thống kê**: Thêm màn hình thống kê mới với bộ lọc Ngày/Tuần/Tháng/Năm.
- 🗂 **Bottom Navigation**: 2 tabs "Phòng khám" và "Thống kê" để chuyển đổi dễ dàng.
- 📅 **Bộ chọn tháng**: Dropdown chọn tháng khi xem thống kê theo ngày.
- 📋 **Bảng số liệu**: Hiển thị dữ liệu thống kê với header, alternate row colors, và tổng cộng.

### Technical
- Thêm `MainScreen.kt` với Scaffold + NavigationBar.
- Thêm `StatsScreen.kt` với Filter Chips + Data Table UI.
- Thêm `StatsRepository.kt` với client-side stats calculations.
- Thêm `StatItem.kt` data model.
- Refactor `MainActivity.kt` để dùng MainScreen.

---

## [v1.0 - 2026-02-06]
### Added
- 🔄 **Infinite Scroll**: Hỗ trợ tải thêm bệnh nhân khi cuộn xuống cuối danh sách (mặc định 50 records/lần).
- 🧬 **Tách biệt Chẩn đoán & Đơn thuốc**: UI Detail giờ đây hiển thị rõ ràng 2 phần này.
- 📅 **Ngày khám**: Hiển thị thêm dòng ngày khám dựa trên thời gian tạo bản ghi.
- 📜 **Thanh cuộn Dialog**: Hỗ trợ xem nội dung dài trong PatientDetailDialog.
- 🛠 **Tài liệu dự án**: Thêm `README.md` và `STRUCTURE.md`.

### Changed
- 💅 **Branding**: Đổi tiêu đề app thành **"PK Ngọc Trường"**.
- 🛠 **UI Fix**: Thêm `statusBarsPadding` để chống đè status bar trên các dòng máy màn hình tràn viền.
- 📋 **Logic Copy**: Nút "Copy Toa" giờ chỉ lấy phần đơn thuốc, không lấy kèm chẩn đoán.

### Fixed
- 🐛 **Sync Date Error**: (Bên phía Python source) Đã fix lỗi không sync được bệnh nhân có ngày sinh nhập dạng văn bản (VD: "18 tháng") lên Supabase.
