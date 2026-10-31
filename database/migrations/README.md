# Database Migration Strategy

## Goal
- `init.sql` is for **fresh reset only** (drops and recreates database).
- Any future DB change must be added as a **new migration file** in this folder.

## Naming Convention
- Use: `V<version>__<short_description>.sql`
- Examples:
  - `V2__add_user_phone.sql`
  - `V3__add_internship_location.sql`

## Rules
1. Never edit old migration files after they are applied.
2. Never put future incremental changes in `init.sql`.
3. One logical change per migration file.

## How to Apply
1. For a clean reset (local/dev):
   - Run `database/init.sql`
2. For existing DB upgrade:
   - Run pending files from this folder in version order (`V2`, `V3`, ...).
