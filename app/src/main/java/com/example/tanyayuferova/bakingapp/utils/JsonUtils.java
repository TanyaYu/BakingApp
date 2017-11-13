package com.example.tanyayuferova.bakingapp.utils;

import android.content.Context;
import android.util.JsonReader;

import com.example.tanyayuferova.bakingapp.entity.Ingredient;
import com.example.tanyayuferova.bakingapp.entity.Recipe;
import com.example.tanyayuferova.bakingapp.entity.Step;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Tanya Yuferova on 11/9/2017.
 */

public class JsonUtils {

    /**
     * Reads data from json file and creates Recipe objects
     * @param context
     * @return List of Recipe objects
     */
    public static List<Recipe> getAllRecipes(Context context) {
        List<Recipe> result = new ArrayList<>();
        JsonReader reader;
        try {
            reader = readJSONFile(context);
            reader.beginArray();
            while (reader.hasNext()) {
                result.add(readRecipe(reader));
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Last Recipes should be on top
        Collections.sort(result);
        Collections.reverse(result);
        return result;
    }

    /**
     * Reads Recipe from JsonReader
     * @param reader JsonReader
     * @return Recipe object
     */
    private static Recipe readRecipe(JsonReader reader) {
        Recipe result = new Recipe();

        try {
            reader.beginObject();
            while (reader.hasNext()) {
                String name = reader.nextName();
                switch (name) {
                    case "name":
                        result.setName(reader.nextString());
                        break;
                    case "id":
                        result.setId(reader.nextInt());
                        break;
                    case "servings":
                        result.setServings(reader.nextInt());
                        break;
                    case "image":
                        result.setImage(reader.nextString());
                        break;
                    case "ingredients":
                        result.setIngredients(getIngredients(reader));
                        break;
                    case "steps":
                        result.setSteps(getSteps(reader));
                        break;
                    default:
                        reader.skipValue();
                        break;
                }
            }
            reader.endObject();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * Reads Ingredients from JsonReader
     * @param reader JsonReader
     * @return List of Ingredients
     */
    private static List<Ingredient> getIngredients(JsonReader reader) {
        List<Ingredient> result = new ArrayList<>();
        try {
            reader.beginArray();
            while (reader.hasNext()) {
                result.add(readIngredient(reader));
            }
            reader.endArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Reads list of Steps from JsonReader
     * @param reader JsonReader
     * @return List of Steps
     */
    private static List<Step> getSteps(JsonReader reader) {
        List<Step> result = new ArrayList<>();
        try {
            reader.beginArray();
            while (reader.hasNext()) {
                result.add(readStep(reader));
            }
            reader.endArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Collections.sort(result);
        return result;
    }

    /**
     * Reads Ingredient from JsonReader
     * @param reader JsonReader
     * @return Ingredient object
     */
    private static Ingredient readIngredient(JsonReader reader) {
        Ingredient result = new Ingredient();

        try {
            reader.beginObject();
            while (reader.hasNext()) {
                String name = reader.nextName();
                switch (name) {
                    case "ingredient":
                        result.setName(reader.nextString());
                        break;
                    case "quantity":
                        result.setQuantity(reader.nextDouble());
                        break;
                    case "measure":
                        result.setMeasure(reader.nextString());
                        break;
                    default:
                        reader.skipValue();
                        break;
                }
            }
            reader.endObject();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * Reads Step from JsonReader
     * @param reader JsonReader
     * @return Step object
     */
    private static Step readStep(JsonReader reader) {
        Step result = new Step();

        try {
            reader.beginObject();
            while (reader.hasNext()) {
                String name = reader.nextName();
                switch (name) {
                    case "id":
                        result.setId(reader.nextInt());
                        break;
                    case "description":
                        result.setDescription(reader.nextString());
                        break;
                    case "shortDescription":
                        result.setShortDescription(reader.nextString());
                        break;
                    case "videoURL":
                        result.setVideoURL(reader.nextString());
                        break;
                    case "thumbnailURL":
                        result.setThumbnailURL(reader.nextString());
                        break;
                    default:
                        reader.skipValue();
                        break;
                }
            }
            reader.endObject();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * Method for creating a JsonReader object that points to the JSON array of samples.
     * @param context The application context.
     * @return The JsonReader object pointing to the JSON array of samples.
     * @throws IOException Exception thrown if the sample file can't be found.
     */
    private static JsonReader readJSONFile(Context context) throws IOException {
        JsonReader reader = null;

        try {
            InputStream is = context.getAssets().open("data.json");
            reader = new JsonReader(new InputStreamReader(is, "UTF-8"));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return reader;
    }
}
