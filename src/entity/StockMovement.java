package entity;

import java.sql.Date;

public class StockMovement {

    private int movementId;
    private String movementType;
    private int quantity;
    private Date movementDate;
    private int inventoryId;

    public StockMovement(int movementId, String movementType,
                         int quantity, Date movementDate, int inventoryId) {
        this.movementId = movementId;
        this.movementType = movementType;
        this.quantity = quantity;
        this.movementDate = movementDate;
        this.inventoryId = inventoryId;
    }

    public int getMovementId() { return movementId; }
    public String getMovementType() { return movementType; }
    public int getQuantity() { return quantity; }
    public Date getMovementDate() { return movementDate; }
    public int getInventoryId() { return inventoryId; }
}