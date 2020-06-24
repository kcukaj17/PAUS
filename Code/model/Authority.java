package model;

public enum Authority {
    ADMIN, LEAD, ECONOMIST, MANAGER, WORKER;

    public static Authority fromString(String string) {
        if (string.equalsIgnoreCase("admin")) return ADMIN;
        if (string.equalsIgnoreCase("lead")) return LEAD;
        if (string.equalsIgnoreCase("economist")) return ECONOMIST;
        if (string.equalsIgnoreCase("manager")) return MANAGER;
        if (string.equalsIgnoreCase("worker")) return WORKER;
        return null;
    }

    @Override
    public String toString() {
        if (this == ADMIN) return "admin";
        if (this == LEAD) return "lead";
        if (this == MANAGER) return "manager";
        if (this == ECONOMIST) return "economist";
        return "worker";
    }
}
