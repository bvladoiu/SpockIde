# Tasks

## Moved Database Files - 07/11/2023
Relocated database files to the /static/db/ directory for better organization and accessibility. Modified DatabaseFactory to store SQLite database files (prisma.db, acme.db, contadeal.db, common.db) in the static/db directory instead of a separate databases directory.

## Added Unicorn DAO - 07/10/2023
Added a Unicorn data class and Unicorns DAO-like object with CRUD operations for the acme subpackage. Updated SQLDelight configuration to include the AcmeDatabase. The implementation allows for creating, reading, updating, and deleting unicorn entities in the database.

## Added Acme Tenant with Unicorns - 06/20/2023
Added a new acme tenant with Unicorns entity, mimicking the structure of competences in prisma. Created necessary database files, static content, and updated configuration to support the new tenant.

## Simplified Routing - 06/16/2023
Simplified Routing.kt to only serve JS files from the static/ directory, removing all other routes and functionality. This streamlines the server to focus solely on serving static JavaScript files.

## Renamed Products.sq to Competences.sq - 06/01/2025
Renamed Products.sq to Competences.sq to better reflect its purpose of storing competence entities instead of products. The schema remains the same with id, competence_id, and index_order fields.

## Updated SQLDelight configuration - 05/30/2025
Updated SQLDelight configuration to align with the multitenant database architecture. Created separate database configurations for each tenant (common, contadeal, prisma) and added initial schema files with table definitions and queries.

## Implemented multitenant database - 05/30/2025
Created a multitenant database framework that supports tenant-specific databases (contadeal.db, prisma.db) and a common database (common.db). Implemented Android-like onCreate and onUpgrade methods with a switch-based migration approach. Added DSL-style database operations for easy access.

## Refactored scaffold to page - 06/15/2023
Renamed scaffold() to page() and moved it to Page.kt. The function now calls the scaffold DSL HTML function from html/layout.kt. Updated Routing.kt to use the new page() function.

## Cleaned up common.json - 04/29/2025
Removed unused labels from common.json files in both language directories, keeping only theme-switcher related labels. This ensures a cleaner and more focused translation structure.

## Removed mock data - 05/22/2023
Removed mock data from /static/common directory while preserving theme-switcher related files. Cleaned up navigation.json from components and blog.json, home.json, resources.json from content/en directory.

## Refactored theme switcher - 05/21/2023
Refactored themeswitcher directory to comply with the new file organization. Created a single theme-switcher.json file with label IDs and removed language subdirectories from components. Merged navigation and theme switcher translations into common.json files in each language directory.

## Unified directory structure - 05/20/2023
Restructured common, prisma, and contadeal directories to have consistent organization with components and content directories. Moved all translatable text to content directory with language subdirectories (en/de), and replaced with label IDs in component files. Removed all data directories.

## Added navigation icons - 05/16/2023
Enhanced pages.json files with Material Symbols icons for each navigation item in both language versions.

## Added multilingual navigation - 05/15/2023
Created language-specific directories (en/de) in static/components/data with pages.json files containing navigation items in English and German.

## Refactored static directory - 04/29/2025
Restructured the static directory to support multiple projects. Created prisma, contadeal, and common folders with data/pages and data/components subfolders. Moved JSON files to prisma/data/pages.
