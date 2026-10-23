package entity;

public class Technician {

    private int employeeId;
    private String specialty;
    private String certificationLevel;

    public Technician(int employeeId,
                      String specialty,
                      String certificationLevel) {

        this.employeeId = employeeId;
        this.specialty = specialty;
        this.certificationLevel = certificationLevel;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public String getSpecialty() {
        return specialty;
    }

    public String getCertificationLevel() {
        return certificationLevel;
    }
}