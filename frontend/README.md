# Project Estimate - Next.js Frontend

This is the frontend application for **Project Estimate**, built using **Next.js**. It serves as the user-facing
interface and communicates with a **Spring Boot backend** to provide a full-stack experience. This app makes use of
modern web development practices and technologies to deliver an intuitive, responsive, and feature-rich interface.

## Features

- **Next.js Framework**: Utilizes the React-based framework for building fast and scalable applications.
- **TailwindCSS Integration**: Modern and customizable styling using TailwindCSS.
- **Radix UI Components**: A wide variety of accessible, unstyled components ready to be customized as needed.
- **shadcn/ui Components**: Styled components based on Radix UI.
- **Form Validation**: Seamless form validations using `react-hook-form` and `zod` for efficient error handling.
- **Dynamic Theming**: Supports themes via the `next-themes` library for light/dark mode support.
- **Charts and Data Visualization**: Includes visualizations with recharts for a better user experience.
- **Carousel Support**: Interactive carousels powered by `embla-carousel-react`.
- **Interactivity**: Powered by libraries for rich UI experiences like `@tanstack/react-table` for data tables and
  `@radix-ui` components.

## Prerequisites

To run this application, you need:

- **Node.js**: `>=18`
- **npm**: Installed with Node.js
- A running **Spring Boot backend**

## Configuration

The application relies on environment variables for configuration. To set up your environment:

1. Copy the `.env.example` file to `.env.local`:

   ```bash
   cp .env.example .env.local
   ```

   The default URL in the `.env.example` is already set to work with the local backend configuration:

## Running the Project Using npm

1. Clone the repository:

   ```bash
   git clone https://github.com/fervo-inc/project-estimation
   cd project-estimation/frontend
   ```

2. Install the project dependencies:

   ```bash
   npm install
   ```

3. Start the development server:

   ```bash
   npm run dev
   ```

   The application will be available at [`localhost:3000`](http://localhost:3000).

Make sure to have the backend running @ [`localhost:8080`](http://localhost:8080).

## Building and Deployment

To build and serve the production version of the application:

1. Build the production-ready app:

   ```bash
   npm run build
   ```

2. Start the production server:

   ```bash
   npm run start
   ```

---
