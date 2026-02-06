# Project Structure 🏗️

Dự án tuân thủ mô hình kiến trúc sạch, tách biệt giữa xử lý dữ liệu và hiển thị giao diện.

## 📁 Tổng quan thư mục `com.skul9x.clinicviewer`

### 🔹 `data/` (Data Layer)
Chịu trách nhiệm giao tiếp với Supabase Cloud.
- 📄 **SupabaseClient.kt**: Cấu hình URL và API Key, khởi tạo entry point cho các repository.
- 📁 **model/**: Chứa các schema dữ liệu (DTO).
    - `Patient.kt`: Thông tin bệnh nhân (`diagnosis`, `medicalHistory`, `dob`, `createdAt`...).
    - `Medicine.kt`: Thông tin thuốc trong đơn.
    - `Prescription.kt`: Cấu trúc Header/Detail của đơn thuốc.
- 📁 **repository/**: Tầng trung gian xử lý logic dữ liệu.
    - `PatientRepository.kt`: Triển khai fetch dữ liệu với `limit` và `offset` cho Infinite Scroll.
    - `PrescriptionRepository.kt`: Truy vấn chi tiết đơn thuốc của từng bệnh nhân.

### 🔹 `ui/` (Presentation Layer)
Chứa giao diện Jetpack Compose.
- 📁 **screens/**: Các composable chính của ứng dụng.
    - `HomeScreen.kt`: Màn hình danh sách chính, xử lý trạng thái cuộn và tìm kiếm.
    - `PatientDetailDialog.kt`: Thành phần hiển thị chi tiết, tích hợp thanh cuộn dọc và logic copy clipboard.
- 📁 **theme/**: Định nghĩa thương hiệu của ứng dụng.
    - `Color.kt`: Chứa các mã màu đặc trưng (BrandTeal, BrandDarkBg, HistoryBoxBg).
    - `Theme.kt`: Cấu hình hệ thống theme Material 3 cho toàn ứng dụng.

### 🔹 Root
- 📄 **MainActivity.kt**: Khởi tạo UI, thiết lập `EdgeToEdge` và bọc ứng dụng trong `AppTheme`.

## ⚙️ Quy ước Code
- Dữ liệu luôn được fetch bất đồng bộ thông qua `suspend functions`.
- Trạng thái UI được quản lý bằng `remember` và `mutableStateOf`.
- Sử dụng `statusBarsPadding()` để tương thích tốt với các loại màn hình (tai thỏ, đục lỗ).
