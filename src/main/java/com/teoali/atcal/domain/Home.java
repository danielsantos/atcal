package com.teoali.atcal.domain;

import java.math.BigDecimal;

public class Home {

  private int quantityClients;

  private int quantityClientsWithDebts;

  private BigDecimal sumAmountToReceiveActualMonth;

  private BigDecimal sumAmountToReceiveNextMonth;

  public int getQuantityClients() {
    return quantityClients;
  }

  public void setQuantityClients(int quantityClients) {
    this.quantityClients = quantityClients;
  }

  public int getQuantityClientsWithDebts() {
    return quantityClientsWithDebts;
  }

  public void setQuantityClientsWithDebts(int quantityClientsWithDebts) {
    this.quantityClientsWithDebts = quantityClientsWithDebts;
  }

  public BigDecimal getSumAmountToReceiveActualMonth() {
    return sumAmountToReceiveActualMonth;
  }

  public void setSumAmountToReceiveActualMonth(BigDecimal sumAmountToReceiveActualMonth) {
    this.sumAmountToReceiveActualMonth = sumAmountToReceiveActualMonth;
  }

  public BigDecimal getSumAmountToReceiveNextMonth() {
    return sumAmountToReceiveNextMonth;
  }

  public void setSumAmountToReceiveNextMonth(BigDecimal sumAmountToReceiveNextMonth) {
    this.sumAmountToReceiveNextMonth = sumAmountToReceiveNextMonth;
  }
}
