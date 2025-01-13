-- Vendors
INSERT INTO vendors (id, name, email, phone, address)
VALUES ('1', 'Vendor A', 'vendorA@example.com', '123-456-7890', '123 Main St, Toronto, ON'),
       ('2', 'Vendor B', 'vendorB@example.com', '098-765-4321', '81 King St, Toronto, ON')
ON CONFLICT (id) DO NOTHING;

-- MaterialCatalog
INSERT INTO material_catalogs (id, name, description, category, sub_category, unit_type, unit_price, in_stock,
                               lead_time_days, vendor_id)
VALUES ('1', 'Concrete', 'Construction material', 'Construction', 'Cement', 'kg', 35.00, 10, 5, '1'),
       ('2', 'Steel', 'Structural steel', 'Construction', 'Steel', 'kg', 150, 80, 25, '2')
ON CONFLICT (id) DO NOTHING;

-- Projects
INSERT INTO projects (id, name, description, location, start_date, end_date, status)
VALUES ('1', 'Project A', 'High-rise building', 'Downtown', '2025-01-01', '2025-12-31', 'PLANNED'),
       ('2', 'Project B', 'Warehouse construction', 'Industrial Area', '2025-02-01', '2025-11-30', 'PLANNED')
ON CONFLICT (id) DO NOTHING;

-- ProjectMaterials
INSERT INTO project_materials (id, project_id, material_catalog_id, quantity, unit_price, notes)
VALUES ('1', '1', '1', 100, 12000.00, 'Foundation'),
       ('2', '2', '2', 500, 1250.00, 'Frame structure')
ON CONFLICT (id) DO NOTHING;

-- LaborCategory
INSERT INTO labor_categories (id, name, description)
VALUES ('1', 'Carpenter', ''),
       ('2', 'Electrician', '')
ON CONFLICT (id) DO NOTHING;

-- ProjectLabor
INSERT INTO project_labor (id, project_id, labor_category_id, hourly_rate, estimated_hours)
VALUES ('1', '1', '1', 80, 200),
       ('2', '2', '2', 120, 360)
ON CONFLICT (id) DO NOTHING;
