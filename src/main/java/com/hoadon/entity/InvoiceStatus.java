package com.hoadon.entity;

public enum InvoiceStatus {
    PENDING("Chờ thanh toán"),
    PAID("Đã thanh toán"),
    OVERDUE("Quá hạn");

    private final String label;

    InvoiceStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
