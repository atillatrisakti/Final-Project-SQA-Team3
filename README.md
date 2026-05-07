# Hadir App Automation - Final Project SQA Team 3

Repository ini berisi framework otomasi pengujian untuk aplikasi **Hadir**, yang dikembangkan sebagai bagian dari proyek akhir tim **SQA Team 3**. Framework ini dirancang menggunakan pendekatan **Page Object Model (POM)** dengan **Selenium WebDriver** dan **Java**.

## 🚀 Tech Stack

- **Language**: Java 17
- **Automation Tool**: Selenium WebDriver (4.x)
- **Test Framework**: TestNG
- **Build Tool**: Maven
- **Reporting**: Extent Reports (Spark Reporter)
- **CI/CD**: GitHub Actions
- **Self-Healing**: Healenium (3.x)
- **Database**: PostgreSQL (Dockerized for Healenium)
- **Infrastructure**: Docker & Docker Compose
- **Configuration**: Dotenv-java (Environment Variables)

## 📁 Project Structure

```text
src
├── main/java/com/hadirapp
│   ├── drivers/         # Konfigurasi WebDriver & Strategi Browser
│   ├── pages/           # Page Object Classes (POM)
│   └── utlis/           # Utility classes (WaitUtils, Constants, etc.)
└── test/java/com/hadirapp
    ├── tests/           # Test Classes (TestNG)
    └── utlis/           # Test Utilities (ExtentManager, TestListener)
```

## 🧪 Features & Test Coverage

Proyek ini mencakup pengujian regresi untuk fitur-fitur utama serta implementasi **Self-Healing Automation**:

### 0. Self-Healing Capability (Healenium)
- **Automatic Element Recovery**: Memperbaiki locator yang rusak secara otomatis saat terjadi perubahan UI minor (ID, Class, XPath, dll).
- **Element Learning**: Menyimpan metadata elemen ke database PostgreSQL untuk referensi pemulihan.

### 1. Authentication

- Register
- Login & Logout
- Reset Password

### 2. Attendance (Absensi)

- Absen Masuk (Normal & Block Camera)
- Absen Keluar
- History Absensi
- Koreksi Absen

### 3. Requests (Pengajuan)

- Pengajuan Cuti
- Pengajuan Lembur
- Pengajuan Sakit
- Pengajuan Izin (Off, Pulang Cepat, Terlambat)

## 🛠️ Setup & Installation

1.  **Clone Repository**

    ```bash
    git clone https://github.com/atillatrisakti/Final-Project-SQA-Team3.git
    cd Final-Project-SQA-Team3
    ```

2.  **Environment Setup**
    - Salin file `.env.example` menjadi `.env`.
    - Isi variabel di dalam `.env` dengan kredensial dan URL yang sesuai.

    ```bash
    cp .env.example .env
    ```

3.  **Start Infrastructure (Docker)**
    Pastikan Docker Desktop sudah menyala, lalu jalankan layanan Selenium Grid dan Healenium:

    ```bash
    docker-compose up -d
    ```

4.  **Install Dependencies**
    ```bash
    mvn install
    ```

## 🏃 Running Tests

### Menjalankan Semua Tes

```bash
mvn clean test -DsuiteXmlFile=testng.xml
```

### Menjalankan Modul Spesifik

- **Authentication**: `mvn clean test -DsuiteXmlFile=testng-auth.xml`
- **Attendance**: `mvn clean test -DsuiteXmlFile=testng-attendance.xml`
- **Requests**: `mvn clean test -DsuiteXmlFile=testng-requests.xml`

### Headless Mode (Untuk CI/CD)

```bash
mvn clean test -Dbrowser=chrome-headless
```

Hasil eksekusi tes akan secara otomatis menghasilkan **Extent Report** yang dapat ditemukan di:
`target/extent-reports/Automation-Report.html`

### Healenium Dashboard
Jika Anda menjalankan tes secara lokal dengan Docker, Anda dapat melihat log healing dan manajemen selector melalui:
- **Healenium Reports**: `http://localhost:7878/report`
- **Postgres Database**: Port `15432` (untuk melihat data selector yang terekam)

## 👷 CI/CD with GitHub Actions

Project ini sudah terintegrasi dengan **GitHub Actions**. Tes akan berjalan otomatis setiap kali ada `push` atau `pull request` ke branch `main/master`.

- Pastikan untuk menambahkan **Secrets** di GitHub untuk semua variabel yang ada di file `.env`.

## 📄 Documentation

Untuk detail dokumentasi pengujian lainnya, silakan akses link berikut:

- **System Integration Testing (SIT)**: [Link Dokumen SIT Disini](https://docs.google.com/spreadsheets/d/1IHhT8LjjULdbzYjjXR3h4Ifu9LxToKzygCJ7aqxLbFs/edit?usp=sharing)

---

**SQA Team 3** - _JuaraCoding Final Project_
