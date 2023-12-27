package br.com.project.screenmatch.model;

public enum Category {

    ACTION("Action"),
    ROMANCE("Romance"),
    COMEDY("Comedy"),
    DRAMA("Drama"),
    CRIME("Crime");

    private String omdbCategory;

    Category (String omdbCategory) {
        this.omdbCategory = omdbCategory;
    }

    public static Category fromString(String text) {
        for (Category category : Category.values()) {
            if (category.omdbCategory.equalsIgnoreCase(text)) {
                return category;
            }
        }
        throw new IllegalArgumentException("There was not any category found to the string provided: " + text);
    }

}
