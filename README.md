## Recipes Assignment

## Application Architecture

![Diagram of application architecture](./assets/Recipes%20API%20Diagram.drawio.svg)


## Folder Structure

```
.
└── java/
    ├── base/
    │   └── ...Entry point classes
    ├── db/
    │   ├── sql/
    │   │   └── ...Classes SQL queries
    │   └── ...Main db classes
    ├── routes/
    │   ├── DTOs/
    │   │   └── ...Data transfer objects for JSON parser (GSON)
    │   ├── Tools/
    │   │   └── ...Tools for routes such as HttpResponder
    │   └── ...Route classes, interface and manager
    └── server/
        └── ...Main server functionality and configuration
```