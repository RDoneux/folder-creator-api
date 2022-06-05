package com.filecreatorapi.api;

public class CandidateBuilder {

    private String name;
    private String organisation;
    private Integer score;
    private boolean interventions;
    private boolean presentation;
    private boolean portfolio;
    private boolean outcome;

    public Candidate build() {
        return new Candidate(this);
    }

    public CandidateBuilder name(String name) {
        this.name = name;
        return this;
    }

    public CandidateBuilder organisation(String organisation) {
        this.organisation = organisation;
        return this;
    }

    public CandidateBuilder score(Integer score) {
        this.score = score;
        return this;
    }

    public CandidateBuilder interventions(boolean interventions) {
        this.interventions = interventions;
        return this;
    }

    public CandidateBuilder presentation(boolean presentation) {
        this.presentation = presentation;
        return this;
    }

    public CandidateBuilder portfolio(boolean portfolio) {
        this.portfolio = portfolio;
        return this;
    }

    public CandidateBuilder outcome(boolean outcome) {
        this.outcome = outcome;
        return this;
    }

    public String getName() {
        return name;
    }

    public String getOrganisation() {
        return organisation;
    }

    public Integer getScore() {
        return score;
    }

    public Boolean getInterventions() {
        return interventions;
    }

    public Boolean getPresentation() {
        return presentation;
    }

    public Boolean getPortfolio() {
        return portfolio;
    }

    public Boolean getOutcome() {
        return outcome;
    }

}
