# Invoice Application Monorepo

This repository combines both the backend and frontend components of the Invoice Application into a single monorepo structure.

## Repository Structure

```
├── backend/     # Java Spring Boot backend application
├── frontend/    # Angular frontend application
└── README.md    # This file
```

## Getting Started

### Backend
The backend is a Java Spring Boot application located in the `backend/` directory.

```bash
cd backend/
# Follow backend-specific setup instructions
```

### Frontend
The frontend is an Angular application located in the `frontend/` directory.

```bash
cd frontend/
npm install
ng serve
```

## Development

Each component (backend/frontend) maintains its own build configuration, dependencies, and deployment instructions. Refer to the individual README files in each directory for specific setup and development instructions.

## History

This monorepo was created by combining two separate repositories:
- Backend: https://github.com/mehinp/invoice-app.git
- Frontend: https://github.com/mehinp/invoice-app-frontend.git

All commit history from both original repositories has been preserved.