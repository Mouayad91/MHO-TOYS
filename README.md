#  MHO TOYS - E-Commerce Platform

A modern, full-stack e-commerce web application for toy retail, featuring secure authentication, admin dashboard, and comprehensive product management.

## About

MHO TOYS is a complete e-commerce solution built for toy retailers. The platform provides:

-  **Customer Features**: Product browsing, shopping cart, secure checkout
-  **Admin Dashboard**: User management, product management, order tracking
-  **Security**: JWT authentication, role-based access control, secure data handling
- 📱 **Responsive Design**: Mobile-first approach with modern UI/UX

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

##  Prerequisites

Before running this application, make sure you have:

- **Java 17+** installed
- **Node.js 18+** and npm installed

# MHO TOYS

MHO TOYS is a full-stack e-commerce web application for toy retail. It features secure authentication, an admin dashboard, and product management.

## Tech Stack

**Backend:** Java 17, Spring Boot, Spring Security, Spring Data JPA, PostgreSQL, JWT

**Frontend:** React, Vite, Tailwind CSS, Material-UI, Axios

## How to Run

1. Create a PostgreSQL database named `mho-toys` and update credentials in `backend/src/main/resources/application.properties`.
2. Start the backend:
   ```bash
   cd backend
   mvn spring-boot:run
   ```
3. Start the frontend:
   ```bash
   cd frontend
   npm install
   npm run dev
   ```
4. Open `http://localhost:5174` in your browser.

## Default Accounts

**Admin:**

- Username: admin
- Password: 1253225

**Customer:**

- Username: customer
- Password: Customer@2024!

Contributions are not accepted for this project.
npm run dev
