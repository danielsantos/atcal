package com.teoali.atcal.domain.enums;

public enum Status {
  EM_ABERTO(0, "EM ABERTO"),
  PAGO(1, "PAGO"),
  VENCIDO(2, "VENCIDO");

  private int id;

  private String description;

  Status(int id, String description) {
    this.id = id;
    this.description = description;
  }

  public String getDescription() {
    return description;
  }

  public int getId() {
    return id;
  }

    public static Status fromId(int id) {
        for (Status status : Status.values()) {
        if (status.getId() == id) {
            return status;
        }
        }
        return null;
    }
}
