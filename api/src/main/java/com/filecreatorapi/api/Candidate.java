package com.filecreatorapi.api;

public class Candidate {

    private final String name;
    private final String organisation;
    private final Integer score;
    private final boolean interventions;
    private final boolean presentation;
    private final boolean portfolio;
    private final boolean outcome;

    public Candidate(CandidateBuilder builder) {
        this.name = builder.getName();
        this.organisation = builder.getOrganisation();
        this.score = builder.getScore();
        this.interventions = builder.getInterventions();
        this.presentation = builder.getPresentation();
        this.portfolio = builder.getPortfolio();
        this.outcome = builder.getOutcome();
    }

    public static CandidateBuilder builder () {
        return new CandidateBuilder();
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
