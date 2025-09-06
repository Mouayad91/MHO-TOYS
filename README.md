# 🧸 MHO TOYS - E-Commerce Platform

A modern, full-stack e-commerce web application for toy retail, featuring secure authentication, admin dashboard, and comprehensive product management.

## 📖 About

MHO TOYS is a complete e-commerce solution built for toy retailers. The platform provides:

- 🛒 **Customer Features**: Product browsing, shopping cart, secure checkout
- 👑 **Admin Dashboard**: User management, product management, order tracking
- 🔐 **Security**: JWT authentication, role-based access control, secure data handling
- 📱 **Responsive Design**: Mobile-first approach with modern UI/UX

## 🚀 Tech Stack

### Backend

- **Java 17** - Programming language
- **Spring Boot 3.5.3** - Application framework
- **Spring Security** - Authentication & authorization
- **Spring Data JPA** - Data persistence
- **PostgreSQL** - Database
- **JWT** - Token-based authentication
- **Maven** - Dependency management

### Frontend

- **React 19** - UI framework
- **Vite** - Build tool and dev server
- **Tailwind CSS** - Styling framework
- **Material-UI** - Component library
- **React Router** - Navigation
- **Axios** - HTTP client
- **React Hot Toast** - Notifications

## 📋 Prerequisites

Before running this application, make sure you have:

- **Java 17+** installed
- **Node.js 18+** and npm installed
- **PostgreSQL 12+** running
- **Git** for version control

## 🛠️ Installation & Setup

### 1. Clone the Repository

```bash
git clone https://github.com/Mouayad91/MHO-TOYS.git
cd MHO-TOYS
```

### 2. Database Setup

1. Create a PostgreSQL database named `mho-toys`
2. Update database credentials in `backend/src/main/resources/application.properties`:
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/mho-toys
   spring.datasource.username=your_username
   spring.datasource.password=your_password
   ```

### 3. Backend Setup

```bash
cd backend
mvn clean install
mvn spring-boot:run
```

The backend server will start on `http://localhost:8080`

### 4. Frontend Setup

```bash
cd frontend
npm install
npm run dev
```

The frontend application will start on `http://localhost:5174`

## 🔑 Default Credentials

### Admin Account

- **Username**: `admin`
- **Password**: `1253225`

### Customer Account

- **Username**: `customer`
- **Password**: `Customer@2024!`

## 🌟 Features

### Customer Portal

- Product catalog with search and filtering
- Shopping cart management
- User registration and authentication
- Order history and tracking

### Admin Dashboard

- User management (create, update, disable users)
- Product management (add, edit, delete products)
- Order management and tracking
- Role-based access control

### Security Features

- JWT-based authentication
- Password encryption (BCrypt)
- CSRF protection
- Secure HTTP headers
- Rate limiting on login attempts

## 🏗️ Project Structure

```
MHO-TOYS/
├── backend/                 # Spring Boot backend
│   ├── src/main/java/      # Java source code
│   ├── src/main/resources/ # Configuration files
│   └── pom.xml            # Maven dependencies
├── frontend/               # React frontend
│   ├── src/               # React source code
│   ├── public/            # Static assets
│   └── package.json       # npm dependencies
└── README.md              # This file
```

## 🚀 Running the Application

1. Start PostgreSQL database
2. Run the backend: `cd backend && mvn spring-boot:run`
3. Run the frontend: `cd frontend && npm run dev`
4. Visit `http://localhost:5174` in your browser

## 🔧 Development

### Backend Development

- The backend uses Spring Boot DevTools for hot reloading
- API endpoints are available at `http://localhost:8080/api`
- H2 console available at `http://localhost:8080/h2-console` (development)

### Frontend Development

- Vite provides hot module replacement (HMR)
- Components follow a modular structure
- Tailwind CSS for responsive design

## 📝 API Endpoints

### Public Endpoints

- `POST /api/auth/public/signin` - User login
- `POST /api/auth/public/signup` - User registration

### Protected Endpoints

- `GET /api/admin/users` - Get all users (Admin only)
- `PUT /api/admin/users/{id}/role` - Update user role (Admin only)
- `GET /api/user/profile` - Get user profile

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 👨‍💻 Author

**Mouayad91** - [GitHub Profile](https://github.com/Mouayad91)

## 🐛 Issues & Support

If you encounter any issues or need support:

1. Check the [Issues](https://github.com/Mouayad91/MHO-TOYS/issues) section
2. Create a new issue with detailed description
3. Include steps to reproduce the problem

---

⭐ **Star this repository if you found it helpful!**
