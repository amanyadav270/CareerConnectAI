package com.placement.model;

import java.util.List;

public class EligibilityResult {
    private boolean eligible;
    private List<String> reasons;

    public EligibilityResult(boolean eligible, List<String> reasons) {
        this.eligible = eligible;
        this.reasons = reasons;
    }

    public boolean isEligible() { return eligible; }
    public void setEligible(boolean eligible) { this.eligible = eligible; }

    public List<String> getReasons() { return reasons; }
    public void setReasons(List<String> reasons) { this.reasons = reasons; }
}