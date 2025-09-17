# MHO TOYS - E-Commerce Platform

A modern, full-stack e-commerce web application for toy retail, featuring secure authentication, admin dashboard, and comprehensive product management.

## About

MHO TOYS is a complete e-commerce solution built for toy retailers. The platform provides:

- **Customer Features**: Product browsing, shopping cart, secure checkout
- **Admin Dashboard**: User management, product management, order tracking
- **Security**: JWT authentication, role-based access control, secure data handling
- **Responsive Design**: Mobile-first approach with modern UI/UX

## Tech Stack

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

## Prerequisites

- **PostgreSQL** - Database server

## Quick Start

### Backend Setup

1. Ensure PostgreSQL is running with database `mho_toys_db`
2. Update database credentials in `backend/src/main/resources/application.properties`
3. Set environment variables:
   - `DB_USERNAME` (default: postgres)
   - `DB_PASSWORD` (required)
   - `JWT_SECRET` (required - 64+ characters)
   - `ADMIN_PASSWORD` (required)
4. Start backend:
   ```bash
   cd backend
   mvn spring-boot:run
   ```

### Frontend Setup

1. Install dependencies and start:
   ```bash
   cd frontend
   npm install
   npm run dev
   ```
2. Open `http://localhost:5174` in your browser

## Default Test Account

**Customer Test Account:**

- Username: customer
- Password: Customer@2024!

**Admin Account:** Created automatically using your `ADMIN_PASSWORD` environment variable.

## Security Notice

This application uses environment variables for sensitive configuration. Ensure all required environment variables are set before deployment.

---

**Note:** This is a personal project. Contributions are not accepted.
