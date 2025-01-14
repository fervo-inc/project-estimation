# Running GitHub Actions Workflows Locally with Act

This guide explains how to run GitHub Actions workflows locally using the [act](https://github.com/nektos/act) CLI. Act
simulates GitHub Actions on your local machine, allowing you to validate and debug workflows without needing to push
commits to your repository.

## Prerequisites

Before using Act, ensure the following prerequisites are met:

1. **Docker Installed and Running**  
   Ensure Docker is installed and running. You can verify with:
   ```bash
   docker info
   ```

2. **Install the Act CLI**  
   Follow the installation guide for Act:
    - **Windows** (with Chocolatey):
      ```bash
      choco install act-cli
      ```
    - **macOS** (with Homebrew):
      ```bash
      brew install act
      ```
    - **Linux** (with curl):
      ```bash
      curl -s https://raw.githubusercontent.com/nektos/act/master/install.sh | sudo bash
      ```

3. **Navigate to the repository root**  
   Ensure you are in the project’s root directory, where the `.github/workflows/` folder exists:

---

## Setting Up Secrets

The docker actions to publish the images use secrets (e.g., `CR_USERNAME`, `CR_PAT`), Act requires these secrets to be
provided locally. Create a `.secrets` file in your project root directory for this purpose.

### `.secrets.example` File

You can find an example of the required secrets in the `.secrets.example` file provided in the repository:

### Steps to Configure Secrets:

1. Copy the `.secrets.example` file to `.secrets` in your repository root:
   ```bash
   cp .secrets.example .secrets
   ```

2. Replace the placeholder values (`<your-container-registry-username>`, `<your-personal-access-token>`) with actual
   values.

---

## Running Workflows with Act

### Run All Jobs in the Workflow

---

### Frontend Workflows

Run the frontend-specific workflows individually using the following commands:

- Build the frontend:
  ```bash
  act push -j build-frontend --defaultbranch master
  ```
- Run the frontend Docker workflow:
  ```bash
  act push -j frontend-docker
  ```
- Run both frontend jobs in sequence:
  ```bash
  act push -j build-frontend --defaultbranch master -j frontend-docker
  ```

---

### Backend Workflows

Run the backend-specific workflows individually using the following commands:

- Build the backend:
  ```bash
  act push -j backend-build
  ```
- Run the backend Docker workflow:
  ```bash
  act push -j backend-docker
  ```
- Run both backend jobs in sequence:
  ```bash
  act push -j backend-build -j backend-docker
  ```

---

To run all jobs defined in your workflow (e.g., triggered by a `push` event):

```bash
act push
```

### Run Specific Jobs

If you want to run only specific jobs from the workflow, use the `-j` flag with the name of the job. Examples:

- Run **frontend-build**:
  ```bash
  act push -j build-frontend --defaultbranch master
  ```
- Run **frontend-docker**:
  ```bash
  act push -j frontend-docker
  ```

If you want to run multiple frontend jobs in sequence:

```bash
act push -j build-frontend --defaultbranch master -j frontend-docker
```

### Simulate Other Events

Act can simulate other GitHub trigger events like `pull_request`. For example:

```bash
act pull_request
```

You can also set environment variables to customize the event trigger:

```bash
act push --env GITHUB_REF=refs/heads/frontend
```

---

## Debugging Workflows with Verbose Mode

Run Act in verbose mode to get more detailed output and help with debugging problems in your workflows:

```bash
act push --verbose
```

### Dry Run

If you want to preview the workflow steps without executing them, use the `--dryrun` option:

```bash
act push --dryrun
```

---

## Troubleshooting Act

### Act Cannot Find the Workflow Files

- Ensure your workflow YAML files are located in `.github/workflows/` at the root of the project.
- Run Act from the root directory of the repository.

### Docker Issues

- Ensure Docker is running correctly with `docker info`.
- Verify your system has enough resources (CPU, memory, disk space) for Docker containers.

### Secrets Not Found

- Ensure the `.secrets` file exists in the root directory.
- Verify all required secrets, as defined in `.secrets.example`, are properly set.

### Windows-Specific Note

For Windows users, ensure Docker Desktop is running, and Act is properly installed. Use `PowerShell` or `Git Bash` to
run the commands.

---

For further details and advanced configuration, visit the [Act Documentation](https://github.com/nektos/act).