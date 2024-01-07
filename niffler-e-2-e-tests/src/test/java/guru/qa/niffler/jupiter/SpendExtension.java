package guru.qa.niffler.jupiter;

import guru.qa.niffler.api.SpendApi;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;
import okhttp3.OkHttpClient;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.platform.commons.support.AnnotationSupport;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.util.Date;
import java.util.Objects;
import java.util.Optional;

public class SpendExtension implements BeforeEachCallback {

    public static final ExtensionContext.Namespace NAMESPACE
            = ExtensionContext.Namespace.create(SpendExtension.class);

    private static final OkHttpClient httpClient = new OkHttpClient.Builder().build();
    private static final Retrofit retrofit = new Retrofit.Builder()
            .client(httpClient)
            .baseUrl("http://127.0.0.1:8093")
            .addConverterFactory(JacksonConverterFactory.create())
            .build();

    private final SpendApi spendApi = retrofit.create(SpendApi.class);

    @Override
    public void beforeEach(ExtensionContext extensionContext) throws Exception {
        Optional<GenerateSpend> spend = AnnotationSupport.findAnnotation(
                extensionContext.getRequiredTestMethod(),
                GenerateSpend.class
        );

        Optional<GenerateCategory> category = AnnotationSupport.findAnnotation(
                extensionContext.getRequiredTestMethod(),
                GenerateCategory.class
        );

        if (spend.isPresent() && category.isEmpty()) {
            GenerateSpend spendData = spend.get();
            SpendJson spendJson = new SpendJson(
                    null,
                    new Date(),
                    spendData.category(),
                    spendData.currency(),
                    spendData.amount(),
                    spendData.description(),
                    spendData.username()
            );

            SpendJson created = spendApi.addSpend(spendJson).execute().body();
            extensionContext.getStore(NAMESPACE)
                    .put("spend", created);
        }

        if (spend.isPresent() && category.isPresent()) {
            GenerateSpend spendData = spend.get();
            GenerateCategory categoryData = category.get();
            SpendJson spendJson = new SpendJson(
                    null,
                    new Date(),
                    categoryData.category(),
                    spendData.currency(),
                    spendData.amount(),
                    spendData.description(),
                    spendData.username()
            );

            CategoryJson categoryJson = new CategoryJson(
                    null,
                    categoryData.category(),
                    categoryData.username()
            );

            if (Objects.requireNonNull(spendApi.getCategories(categoryData.username()).execute().body()).isEmpty()) {
                CategoryJson createdCategory = spendApi.addCategory(categoryJson).execute().body();
                extensionContext.getStore(NAMESPACE).put("category", createdCategory);
            }

            SpendJson createdSpend = spendApi.addSpend(spendJson).execute().body();
            extensionContext.getStore(NAMESPACE)
                    .put("spend", createdSpend);
        }
    }
}
