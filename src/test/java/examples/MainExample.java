package examples;

import de.kherud.llama.ModelResolver;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import de.kherud.llama.InferenceParameters;
import de.kherud.llama.LlamaModel;
import de.kherud.llama.ModelParameters;

public class MainExample {

    public static void main(String... args) throws IOException {
        LlamaModel.setLogger((level, message) -> System.out.print(message));
        ModelParameters modelParams = new ModelParameters()
                .setNGpuLayers(43);
        InferenceParameters inferParams = new InferenceParameters()
                .setTemperature(0.7f)
                .setPenalizeNl(true)
//                .setNProbs(10)
                .setMirostat(InferenceParameters.MiroStat.V2)
                .setAntiPrompt("User:");
        //String modelName = System.getProperty("model.name");
        String modelName = "mistral-7b-instruct-v0.2.Q2_K.gguf";
        //String modelName = "stable-code-3b.Q2_K.gguf";
        //String modelName  ="llama-2-7b.Q2_K.gguf";
        String modelPath = ModelResolver.getPathToModel(modelName);
        String system = "This is a conversation between User and Llama, a friendly chatbot.\n" +
                "Llama is helpful, kind, honest, good at writing, and never fails to answer any " +
                "requests immediately and with precision.\n\n" +
                "User: Hello Llama\n" +
                "Llama: Hello.  How may I help you today?";
                ;
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
        try (LlamaModel model = new LlamaModel(modelPath, modelParams)) {
            System.out.print(system);
            String prompt = system;
            while (true) {
                prompt += "\nUser: ";
                System.out.print("\nUser: ");
                String input = reader.readLine();
                prompt += input;
                System.out.print("Llama: ");
                prompt += "\nLlama: ";
//                String answer = model.complete(prompt, inferParams);
//                System.out.print(answer);
//                prompt += answer;
                for (LlamaModel.Output output : model.generate(prompt, inferParams)) {
                    System.out.print(output);
                    prompt += output;
                }
            }
        }
    }
}
