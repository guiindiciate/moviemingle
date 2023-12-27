package br.com.project.screenmatch.service;

import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.service.OpenAiService;

public class ChatGPTQuery {

    public static String translationObtain(String text) {
        OpenAiService service = new OpenAiService(System.getenv("OPENAI_APIKEY"));

        CompletionRequest request = CompletionRequest.builder()
                .model("text-davinci-003")
                .prompt("translate to languageChoice: " + text)
                .maxTokens(1000)
                .temperature(0.7)
                .build();

        var response = service.createCompletion(request);
        return response.getChoices().get(0).getText();
    }
}
