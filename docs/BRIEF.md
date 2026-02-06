# 💡 BRIEF: ClinicViewer Statistics Feature

**Ngày tạo:** 2026-02-06  
**Tham chiếu:** Tính năng Thống kê từ `ClinicManagerv5.0_SourceCode`

---

## 1. VẤN ĐỀ CẦN GIẢI QUYẾT

Hiện tại app ClinicViewer chỉ có 1 màn hình xem danh sách bệnh nhân. Cần:
- Thêm tính năng **Thống kê** (copy từ app Python)
- Cải thiện UI với **2 tabs** để dễ sử dụng

---

## 2. GIẢI PHÁP ĐỀ XUẤT

### 2.1 Cấu trúc 2 Tabs

| Tab | Nội dung | Icon |
|-----|----------|------|
| **📋 Phòng khám** | Danh sách bệnh nhân hiện tại (HomeScreen) | `Icons.Default.People` |
| **📊 Thống kê** | Báo cáo thống kê từ Supabase | `Icons.Default.Analytics` |

### 2.2 Tính năng Thống kê (từ Python app)

| Loại thống kê | Mô tả | Query từ Supabase |
|---------------|-------|-------------------|
| 📅 Theo Ngày | Số lượt khám theo từng ngày trong tháng | `patients` group by `DATE(created_at)` |
| 📅 Theo Tuần | Số lượt khám theo tuần | `patients` group by `WEEK(created_at)` |
| 📅 Theo Tháng | Số lượt khám theo tháng | `patients` group by `MONTH(created_at)` |
| 📅 Theo Năm | Số lượt khám theo năm | `patients` group by `YEAR(created_at)` |
| 👶 Theo Độ Tuổi | Phân bố theo nhóm tuổi | Complex: tính từ `dob` |
| ⚧ Giới Tính | Phân bố Nam/Nữ | `patients` group by `gender` |
| 📍 Địa Điểm | Top địa điểm có nhiều bệnh nhân | `patients` group by `address` |

---

## 3. THIẾT KẾ UI ĐỀ XUẤT

### 3.1 Bottom Navigation Bar (2 Tabs)//
```
┌─────────────────────────────────────┐
│                                     │
│         [Main Content Area]         │
│                                     │
├───────────────┬─────────────────────┤
│  📋 Phòng khám │   📊 Thống kê      │
└───────────────┴─────────────────────┘
```

### 3.2 Tab Thống kê - Layout

```
┌─────────────────────────────────────┐
│  📊 Thống Kê Phòng Khám            │
├─────────────────────────────────────┤
│ [Chip: Ngày] [Tuần] [Tháng] [Năm]  │
│ [Chip: Tuổi] [Giới tính] [Địa điểm] │
├─────────────────────────────────────┤
│  🗓 Chọn tháng: [Dropdown: 01/2026] │
├─────────────────────────────────────┤
│                                     │
│   ┌───────────────────┬─────────┐  │
│   │ 05/02/2026 (T5)   │   12    │  │
│   ├───────────────────┼─────────┤  │
│   │ 04/02/2026 (T4)   │    8    │  │
│   ├───────────────────┼─────────┤  │
│   │ 03/02/2026 (T3)   │   15    │  │
│   ├───────────────────┼─────────┤  │
│   │ TỔNG CỘNG         │   35    │  │
│   └───────────────────┴─────────┘  │
│                                     │
└─────────────────────────────────────┘
```

### 3.3 Màu sắc & Style (Premium Dark Theme)

- **Background:** `#1A1E25` (BrandDarkBg)
- **Cards:** `#252A34` (CardBackground)  
- **Accent:** `#00BFA5` (BrandTeal)
- **Filter Chips:** Glassmorphism + Teal border khi selected
- **Data Table:** Alternate row colors + bold total row

---

## 4. CẤU TRÚC CODE ĐỀ XUẤT

### Files cần tạo/sửa:

| Action | File | Mô tả |
|--------|------|-------|
| **[NEW]** | `ui/screens/StatsScreen.kt` | Màn hình Thống kê |
| **[NEW]** | `ui/screens/MainScreen.kt` | Container với 2 Tabs |
| **[NEW]** | `data/repository/StatsRepository.kt` | Repository cho stats queries |
| **[NEW]** | `data/model/StatItem.kt` | Data class cho thống kê |
| **[MODIFY]** | `MainActivity.kt` | Chuyển sang dùng MainScreen |
| **[KEEP]** | `ui/screens/HomeScreen.kt` | Không đổi - đã hoàn thiện |

### Dependency:

```kotlin
// Nếu muốn hiển thị biểu đồ (optional)
implementation("com.patrykandpatrick.vico:compose-m3:1.13.1")
```

---

## 5. CÂU HỎI CHO ANH

Trước khi em lên kế hoạch chi tiết, anh cho em biết ý kiến:

1. **Giao diện tabs:** Anh muốn dùng **Bottom Navigation** (như trên) hay **Top Tabs**?

2. **Biểu đồ:** Anh có muốn thêm **biểu đồ cột/đường** không, hay chỉ cần bảng số liệu là đủ?

3. **Thống kê nào ưu tiên?** Em sẽ làm hết 7 loại, nhưng nếu thời gian có hạn, anh muốn ưu tiên loại nào?
   - [ ] Theo Ngày/Tuần/Tháng/Năm  
   - [ ] Theo Độ Tuổi
   - [ ] Theo Giới Tính
   - [ ] Theo Địa Điểm

4. **Supabase RPC:** Một số thống kê phức tạp (như tuổi) cần tính toán. Anh muốn:
   - **A)** Tính toán phía client (fetch all patients → calculate) - Đơn giản nhưng chậm nếu data lớn
   - **B)** Tạo RPC function trên Supabase - Nhanh hơn nhưng cần setup thêm

---

## 6. BƯỚC TIẾP THEO

Sau khi anh trả lời câu hỏi, em sẽ:
1. Chạy `/plan` để tạo thiết kế chi tiết
2. Chạy `/design` để thiết kế database queries
3. Chạy `/code` để implement
