package br.com.project.screenmatch.model;

public enum Category {

    ACTION("Action", "Ação"),
    ROMANCE("Romance", "Romance"),
    COMEDY("Comedy", "Comédia"),
    DRAMA("Drama", "Drama"),
    CRIME("Crime", "Crime");

    private String omdbCategory;
    private String categoryPortuguese;

    Category (String omdbCategory, String categoryPortuguese) {

        this.omdbCategory = omdbCategory;
        this.categoryPortuguese = categoryPortuguese;
    }

    public static Category fromString(String text) {
        for (Category category : Category.values()) {
            if (category.omdbCategory.equalsIgnoreCase(text)) {
                return category;
            }
        }
        throw new IllegalArgumentException("There was not any category found to the string provided: " + text);
    }
//    public static Category fromPortuguese(String text) {
//        for (Category category : Category.values()) {
//            if (category.categoryPortuguese.equalsIgnoreCase(text)) {
//                return category;
//            }
//        }
//        throw new IllegalArgumentException("There was not any category found to the string provided: " + text);
//    }


}
