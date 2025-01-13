# Use an official Node.js image as the base image
#FROM node:18-alpine AS builder
FROM oven/bun:canary-alpine AS builder

# Set the working directory inside the container
WORKDIR /app

# Copy package.json and bun.lock to install dependencies
COPY package.json ./

COPY bun.lockb* ./

# Install dependencies
RUN bun install

# Copy the rest of the files into the container
COPY . .

# Build the application
RUN bun run build

# Use a lightweight Node.js image for the production environment
#FROM node:18-alpine AS runner
FROM oven/bun:canary-alpine AS runner

# Set the working directory
WORKDIR /app

# Copy the build output and dependencies from the builder stage
COPY --from=builder /app/.next ./.next
COPY --from=builder /app/public ./public
COPY --from=builder /app/package.json ./package.json

# Install only production dependencies
RUN bun install --production --legacy-peer-deps

# Expose the default Next.js port
EXPOSE 3000

# Command to start the application
CMD ["bun", "run", "start"]