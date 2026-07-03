-- ── 2. BONUS: TRIGGER IMPLEMENTATION ────────────────────────

CREATE OR REPLACE FUNCTION check_duplicate_username()
RETURNS TRIGGER AS $$
BEGIN
    IF EXISTS (SELECT 1 FROM UsersTable WHERE USERNAME = NEW.USERNAME AND UID <> NEW.UID) THEN
        RAISE EXCEPTION 'Registration Failed: Username % is already taken.', NEW.USERNAME;
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_check_username
BEFORE INSERT OR UPDATE ON UsersTable
FOR EACH ROW
EXECUTE FUNCTION check_duplicate_username();