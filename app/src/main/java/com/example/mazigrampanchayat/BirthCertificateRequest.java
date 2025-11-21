package com.example.mazigrampanchayat;

/**
 * Model class for Birth Certificate requests
 * Stores all information required for birth certificate generation
 */
public class BirthCertificateRequest {
    private String userId;
    private String requestDate;
    private String status; // "pending", "approved", "rejected"
    private ChildInfo childInfo;
    private MotherInfo motherInfo;
    private FatherInfo fatherInfo;
    private AddressInfo addressInfo;
    private String remarks;
    private String registrationNumber;
    private String registrationDate;
    private String certificatePdfBase64;
    private String issuingAuthority;
    private String requestId;

    public BirthCertificateRequest() {
        // Default constructor required for Firebase
    }

    // Getters and Setters
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(String requestDate) {
        this.requestDate = requestDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ChildInfo getChildInfo() {
        return childInfo;
    }

    public void setChildInfo(ChildInfo childInfo) {
        this.childInfo = childInfo;
    }

    public MotherInfo getMotherInfo() {
        return motherInfo;
    }

    public void setMotherInfo(MotherInfo motherInfo) {
        this.motherInfo = motherInfo;
    }

    public FatherInfo getFatherInfo() {
        return fatherInfo;
    }

    public void setFatherInfo(FatherInfo fatherInfo) {
        this.fatherInfo = fatherInfo;
    }

    public AddressInfo getAddressInfo() {
        return addressInfo;
    }

    public void setAddressInfo(AddressInfo addressInfo) {
        this.addressInfo = addressInfo;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public String getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(String registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getCertificatePdfBase64() {
        return certificatePdfBase64;
    }

    public void setCertificatePdfBase64(String certificatePdfBase64) {
        this.certificatePdfBase64 = certificatePdfBase64;
    }

    public String getIssuingAuthority() {
        return issuingAuthority;
    }

    public void setIssuingAuthority(String issuingAuthority) {
        this.issuingAuthority = issuingAuthority;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    // Inner classes for nested data
    public static class ChildInfo {
        private String nameMarathi;
        private String nameEnglish;
        private String dateOfBirth;
        private String placeOfBirth;
        private String sex;

        public ChildInfo() {}

        public ChildInfo(String nameMarathi, String nameEnglish, String dateOfBirth, 
                        String placeOfBirth, String sex) {
            this.nameMarathi = nameMarathi;
            this.nameEnglish = nameEnglish;
            this.dateOfBirth = dateOfBirth;
            this.placeOfBirth = placeOfBirth;
            this.sex = sex;
        }

        // Getters and Setters
        public String getNameMarathi() { return nameMarathi; }
        public void setNameMarathi(String nameMarathi) { this.nameMarathi = nameMarathi; }
        public String getNameEnglish() { return nameEnglish; }
        public void setNameEnglish(String nameEnglish) { this.nameEnglish = nameEnglish; }
        public String getDateOfBirth() { return dateOfBirth; }
        public void setDateOfBirth(String dateOfBirth) { this.dateOfBirth = dateOfBirth; }
        public String getPlaceOfBirth() { return placeOfBirth; }
        public void setPlaceOfBirth(String placeOfBirth) { this.placeOfBirth = placeOfBirth; }
        public String getSex() { return sex; }
        public void setSex(String sex) { this.sex = sex; }
    }

    public static class MotherInfo {
        private String nameMarathi;
        private String nameEnglish;
        private String aadhaarMasked;

        public MotherInfo() {}

        public MotherInfo(String nameMarathi, String nameEnglish, String aadhaarMasked) {
            this.nameMarathi = nameMarathi;
            this.nameEnglish = nameEnglish;
            this.aadhaarMasked = aadhaarMasked;
        }

        // Getters and Setters
        public String getNameMarathi() { return nameMarathi; }
        public void setNameMarathi(String nameMarathi) { this.nameMarathi = nameMarathi; }
        public String getNameEnglish() { return nameEnglish; }
        public void setNameEnglish(String nameEnglish) { this.nameEnglish = nameEnglish; }
        public String getAadhaarMasked() { return aadhaarMasked; }
        public void setAadhaarMasked(String aadhaarMasked) { this.aadhaarMasked = aadhaarMasked; }
    }

    public static class FatherInfo {
        private String nameMarathi;
        private String nameEnglish;
        private String aadhaarMasked;

        public FatherInfo() {}

        public FatherInfo(String nameMarathi, String nameEnglish, String aadhaarMasked) {
            this.nameMarathi = nameMarathi;
            this.nameEnglish = nameEnglish;
            this.aadhaarMasked = aadhaarMasked;
        }

        // Getters and Setters
        public String getNameMarathi() { return nameMarathi; }
        public void setNameMarathi(String nameMarathi) { this.nameMarathi = nameMarathi; }
        public String getNameEnglish() { return nameEnglish; }
        public void setNameEnglish(String nameEnglish) { this.nameEnglish = nameEnglish; }
        public String getAadhaarMasked() { return aadhaarMasked; }
        public void setAadhaarMasked(String aadhaarMasked) { this.aadhaarMasked = aadhaarMasked; }
    }

    public static class AddressInfo {
        private String birthAddress;
        private String permanentAddress;
        private String taluka;
        private String district;
        private String state;
        private String pincode;

        public AddressInfo() {
            this.taluka = "हातकणंगले";
            this.district = "कोल्हापूर";
            this.state = "महाराष्ट्र";
            this.pincode = "416122";
        }

        public AddressInfo(String birthAddress, String permanentAddress) {
            this();
            this.birthAddress = birthAddress;
            this.permanentAddress = permanentAddress;
        }

        // Getters and Setters
        public String getBirthAddress() { return birthAddress; }
        public void setBirthAddress(String birthAddress) { this.birthAddress = birthAddress; }
        public String getPermanentAddress() { return permanentAddress; }
        public void setPermanentAddress(String permanentAddress) { this.permanentAddress = permanentAddress; }
        public String getTaluka() { return taluka; }
        public void setTaluka(String taluka) { this.taluka = taluka; }
        public String getDistrict() { return district; }
        public void setDistrict(String district) { this.district = district; }
        public String getState() { return state; }
        public void setState(String state) { this.state = state; }
        public String getPincode() { return pincode; }
        public void setPincode(String pincode) { this.pincode = pincode; }
    }
}

