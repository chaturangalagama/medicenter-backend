package com.lippo.commons.util;

public enum StatusCode {

    S0000 {
        public String description() {
            return "Success";
        }
    },
    I5000 {
        public String description() {
            return "Internal Error";
        }
    },
    E2000 {
        public String description() {
            return "Record not found";
        }
    },
    E2001 {
        public String description() {
            return "Invalid Attached Medical Coverage";
        }
    },
    E2002 {
        public String description() {
            return "Invalid Clinic Details";
        }
    },
    E2003 {
        public String description() {
            return "Invalid Patient Details";
        }
    },
    E2004 {
        public String description() {
            return "Case ID not valid";
        }
    },
    E1000 {
        public String description() {
            return "User ID already in use";
        }
    },
    E1001 {
        public String description() {
            return "Invalid search type";
        }
    },
    E1002 {
        public String description() {
            return "Invalid parameters";
        }
    },
    E1004 {
        public String description() {
            return "Name already exists";
        }
    },
    E1005 {
        public String description() {
            return "Invalid Input data";
        }
    },
    E1006 {
        public String description() {
            return "Not allowed parameters";
        }
    },
    E1007 {
        public String description() {
            return "Duplicate record";
        }
    },
    E1008 {
        public String description() {
            return "Not allowed to remove";
        }
    },
    E1009 {
        public String description() {
            return "Not allowed to change state";
        }
    },
    E1010 {
        public String description() {
            return "Not Allowed";
        }
    },
    E1011 {
        public String description() {
            return "Prohibited";
        }
    };

    public abstract String description();


}
