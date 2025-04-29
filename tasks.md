# Tasks

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
