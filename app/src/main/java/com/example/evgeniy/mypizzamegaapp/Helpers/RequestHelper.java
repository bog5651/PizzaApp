package com.example.evgeniy.mypizzamegaapp.Helpers;

import android.util.Log;

import com.example.evgeniy.mypizzamegaapp.Models.Pizza;
import com.example.evgeniy.mypizzamegaapp.Models.Product;
import com.example.evgeniy.mypizzamegaapp.Models.User;
import com.example.evgeniy.mypizzamegaapp.Tasks.GetterJSON;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RequestHelper {

    private static final String TAG = "requestHelper";

    //private static final String host = "http://192.168.0.62"; //локальная сеть
    private static final String host = "http://192.168.33.10"; //локальная машина

    private static final String UrlLogin = "/api/login";
    private static final String UrlUser = "/api/user";
    private static final String UrlRegister = "/api/register";
    private static final String UrlLogout = "/api/logout";

    private static final String UrlPizza = "/api/pizza";
    private static final String UrlPizzaStructure = "/api/PizzaStructute";
    private static final String UrlAddPizza = "/api/addPizza";
    private static final String UrlRemoveProductFromPizza = "/api/removeProductFromPizza";

    private static final String UrlProduct = "/api/product";
    private static final String UrlAddProduct = "/api/addProduct";
    private static final String UrlRemoveProduct = "/api/removeProduct";

    public static void apiLogin(String login, String password, final ApiInterface.onCompleteWithResult callback) {
        GetterJSON getter = new GetterJSON(null, null, new GetterJSON.onCompleteEventHandler() {
            @Override
            public void onComplete(String Json) {
                if (Json == null) {
                    callback.onFail("Internet Error");
                    return;
                }
                try {
                    JSONObject result = new JSONObject(Json);
                    if (result.getInt("success") == 1) {
                        String token = result.getString("token");
                        callback.onSuccess(token);
                    } else {
                        callback.onFail(formatError(result.getJSONObject("error")));
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "onCompleteWithResult: exn " + e.getMessage());
                    callback.onFail(e.getMessage());
                }
            }

            @Override
            public void onFail(String error) {
                callback.onFail(error);
            }
        });
        JSONObject JSONtoSend = new JSONObject();
        try {
            JSONtoSend.put("login", login);
            JSONtoSend.put("password", password);
        } catch (JSONException e) {
            Log.e(TAG, "onClick: exn " + e.getMessage());
            callback.onFail(e.getMessage());
        }
        getter.execute(host + UrlLogin, JSONtoSend.toString());
    }

    public static void apiGetUser(String token, final ApiInterface.onCompleteGetUser callback) {
        GetterJSON getter = new GetterJSON(null, null, new GetterJSON.onCompleteEventHandler() {
            @Override
            public void onComplete(String Json) {
                if (Json == null) {
                    callback.onFail("Internet Error");
                    return;
                }
                try {
                    JSONObject result = new JSONObject(Json);
                    if (result.getInt("success") == 1) {
                        JSONObject jsonUser = result.getJSONObject("user");
                        User user = new User();
                        user.firstname = jsonUser.getString("firstname");
                        user.id_user = jsonUser.getInt("id_user");
                        user.login = jsonUser.getString("login");
                        user.secondname = jsonUser.getString("secondname");
                        user.role = jsonUser.getString("name");
                        callback.onSuccess(user);
                    } else {
                        callback.onFail(formatError(result.getJSONObject("error")));
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "onCompleteWithResult: " + e.getMessage());
                    callback.onFail(e.getMessage());
                }
            }

            @Override
            public void onFail(String error) {
                callback.onFail(error);
            }
        });
        JSONObject JSONtoSend = new JSONObject();
        try {
            JSONtoSend.put("token", token);
        } catch (JSONException e) {
            Log.e(TAG, "onClick: exn " + e.getMessage());
        }
        getter.execute(host + UrlUser, JSONtoSend.toString());
    }

    public static void apiGetPizza(String token, final String pizzaId, final ApiInterface.onCompleteGetPizza callback) {
        GetterJSON getter = new GetterJSON(null, null, new GetterJSON.onCompleteEventHandler() {
            @Override
            public void onComplete(String Json) {
                if (Json == null) {
                    callback.onFail("Internet Error");
                    return;
                }
                try {
                    JSONObject result = new JSONObject(Json);
                    if (result.getInt("success") == 1) {
                        JSONArray JSONPizzas = result.getJSONArray("pizza");
                        ArrayList<Pizza> pizzas = new ArrayList<>();
                        for (int i = 0; i < JSONPizzas.length(); i++) {
                            Pizza pizza = new Pizza();
                            pizza.PizzaId = JSONPizzas.getJSONObject(i).getInt("id");
                            pizza.PizzaName = JSONPizzas.getJSONObject(i).getString("name");
                            pizza.PizzaCost = JSONPizzas.getJSONObject(i).getDouble("cost");
                            pizzas.add(pizza);
                        }
                        callback.onSuccess(pizzas);
                    } else {
                        callback.onFail(formatError(result.getJSONObject("error")));
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "onCompleteWithResult: " + e.getMessage());
                    callback.onFail(e.getMessage());
                }
            }

            @Override
            public void onFail(String error) {
                callback.onFail(error);
            }
        });
        JSONObject JSONtoSend = new JSONObject();
        try {
            JSONtoSend.put("token", token);
        } catch (JSONException e) {
            Log.e(TAG, "onClick: exn " + e.getMessage());
            callback.onFail("Ошибка вложения токена");
            return;
        }
        if (pizzaId != null) {
            try {
                JSONtoSend.put("id", pizzaId);
            } catch (JSONException e) {
                Log.d(TAG, "apiGetPizza: " + e.getMessage());
                callback.onFail("Ошибка вложения id пиццы");
                return;
            }
        }
        getter.execute(host + UrlPizza, JSONtoSend.toString());
    }

    public static void apiGetProduct(String token, final String productId, final ApiInterface.onCompleteGetProductList callback) {
        GetterJSON getter = new GetterJSON(null, null, new GetterJSON.onCompleteEventHandler() {
            @Override
            public void onComplete(String Json) {
                if (Json == null) {
                    callback.onFail("Internet Error");
                    return;
                }
                try {
                    JSONObject result = new JSONObject(Json);
                    if (result.getInt("success") == 1) {
                        JSONArray JSONPizzas = result.getJSONArray("product");
                        ArrayList<Product> products = new ArrayList<>();
                        for (int i = 0; i < JSONPizzas.length(); i++) {
                            Product product = new Product();
                            try {
                                product.id = JSONPizzas.getJSONObject(i).getInt("id");
                            } catch (JSONException e) {
                                Log.d(TAG, "onComplete: " + e.getMessage());
                            }
                            product.name = JSONPizzas.getJSONObject(i).getString("product_name");
                            product.cost = JSONPizzas.getJSONObject(i).getInt("cost");
                            product.unit = JSONPizzas.getJSONObject(i).getString("unit");
                            products.add(product);
                        }
                        callback.onSuccess(products);
                    } else {
                        callback.onFail(formatError(result.getJSONObject("error")));
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "onCompleteWithResult: " + e.getMessage());
                    callback.onFail(e.getMessage());
                }
            }

            @Override
            public void onFail(String error) {
                callback.onFail(error);
            }
        });
        JSONObject JSONtoSend = new JSONObject();
        try {
            JSONtoSend.put("token", token);
        } catch (JSONException e) {
            Log.e(TAG, "onClick: exn " + e.getMessage());
            callback.onFail("Ошибка вложения токена");
            return;
        }
        if (productId != null) {
            try {
                JSONObject product = new JSONObject();
                product.put("id", productId);
                JSONtoSend.put("product", product);
            } catch (JSONException e) {
                Log.d(TAG, "apiGetPizza: " + e.getMessage());
                callback.onFail("Ошибка вложения id пиццы");
                return;
            }
        }
        Log.d(TAG, "apiGetProduct: " + JSONtoSend.toString());
        getter.execute(host + UrlProduct, JSONtoSend.toString());
    }

    public static void apiRegister(String login, String password, String firstName, String secondName, final ApiInterface.onCompleteWithResult callback) {
        GetterJSON getterJSON = new GetterJSON(new GetterJSON.onCompleteEventHandler() {
            @Override
            public void onComplete(String Json) {
                if (Json == null) {
                    callback.onFail("Internet Error");
                    return;
                }
                try {
                    JSONObject result = new JSONObject(Json);
                    if (result.getInt("success") == 1) {
                        String token = result.getString("token");
                        if (!token.equals(""))
                            callback.onSuccess(token);
                        else
                            callback.onFail("Вернулся пустой токен");
                    } else {
                        callback.onFail(formatError(result.getJSONObject("error")));
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "onFail: " + e.getMessage());
                    callback.onFail(e.getMessage());
                }
            }

            @Override
            public void onFail(String error) {
                callback.onFail(error);
            }
        });
        JSONObject JSONtoSend = new JSONObject();
        try {
            JSONtoSend.put("login", login);
            JSONtoSend.put("password", password);
            JSONtoSend.put("firstname", firstName);
            JSONtoSend.put("secondname", secondName);
        } catch (JSONException e) {
            Log.e(TAG, "onClick: exn " + e.getMessage());
            callback.onFail("Ошибка вложения данных" + e.getMessage());
            return;
        }
        getterJSON.execute(host + UrlRegister, JSONtoSend.toString());
    }

    public static void apiGetPizzaStruct(String token, int pizzaId, final ApiInterface.onCompleteGetProductList callback) {
        GetterJSON getter = new GetterJSON(null, null, new GetterJSON.onCompleteEventHandler() {
            @Override
            public void onComplete(String Json) {
                if (Json == null) {
                    callback.onFail("Internet Error");
                    return;
                }
                try {
                    JSONObject result = new JSONObject(Json);
                    if (result.getInt("success") == 1) {
                        JSONArray JSONPizzas = result.getJSONArray("pizza");
                        ArrayList<Product> products = new ArrayList<>();
                        for (int i = 0; i < JSONPizzas.length(); i++) {
                            Product product = new Product();
                            product.name = JSONPizzas.getJSONObject(i).getString("product_name");
                            product.count = JSONPizzas.getJSONObject(i).getInt("count_product");
                            product.unit = JSONPizzas.getJSONObject(i).getString("unit");
                            products.add(product);
                        }
                        callback.onSuccess(products);
                    } else {
                        callback.onFail(formatError(result.getJSONObject("error")));
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "onCompleteWithResult: " + e.getMessage());
                    callback.onFail(e.getMessage());
                }
            }

            @Override
            public void onFail(String error) {
                callback.onFail(error);
            }
        });
        JSONObject JSONtoSend = new JSONObject();
        try {
            JSONtoSend.put("token", token);
        } catch (JSONException e) {
            Log.e(TAG, "apiGetPizzaStructure: exp " + e.getMessage());
            callback.onFail("Ошибка вложения токена");
            return;
        }
        try {
            JSONtoSend.put("id", pizzaId);
        } catch (JSONException e) {
            Log.d(TAG, "apiGetPizzaStructure: " + e.getMessage());
            callback.onFail("Ошибка вложения id пиццы");
            return;
        }
        getter.execute(host + UrlPizzaStructure, JSONtoSend.toString());
    }

    public static void apiLogout(String token, final ApiInterface.onComplete callback) {
        GetterJSON getter = new GetterJSON(null, null, new GetterJSON.onCompleteEventHandler() {
            @Override
            public void onComplete(String Json) {
                if (Json == null) {
                    callback.onFail("Internet Error");
                    return;
                }
                try {
                    JSONObject result = new JSONObject(Json);
                    if (result.getInt("success") == 1) {
                        callback.onSuccess();
                    } else {
                        callback.onFail(formatError(result.getJSONObject("error")));
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "onCompleteWithResult: exn " + e.getMessage());
                    callback.onFail(e.getMessage());
                }
            }

            @Override
            public void onFail(String error) {
                callback.onFail(error);
            }
        });
        JSONObject JSONtoSend = new JSONObject();
        try {
            JSONtoSend.put("token", token);
        } catch (JSONException e) {
            Log.e(TAG, "onClick: exn " + e.getMessage());
            callback.onFail(e.getMessage());
        }
        getter.execute(host + UrlLogout, JSONtoSend.toString());
    }

    public static void apiAddPizza(String token, ArrayList<Product> products, String pizzaName, String cost, final ApiInterface.onCompleteWithResult callback) {
        final GetterJSON getterJSON = new GetterJSON(new GetterJSON.onCompleteEventHandler() {
            @Override
            public void onComplete(String Json) {
                if (Json == null) {
                    callback.onFail("Internet Error");
                    return;
                }
                Log.d(TAG, Json);
                try {
                    JSONObject result = new JSONObject(Json);
                    if (result.getInt("success") == 1) {
                        try {
                            String cost = result.getString("cost");
                        } catch (JSONException ignored) {
                            Log.d(TAG, "не надена стоимость пиццы");
                        }
                        int id = result.getInt("id");
                        callback.onSuccess(String.valueOf(id));
                    } else {
                        callback.onFail(formatError(result.getJSONObject("error")));
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "onFail: " + e.getMessage());
                    callback.onFail(e.getMessage());
                }
            }

            @Override
            public void onFail(String error) {
                callback.onFail(error);
            }
        });
        JSONObject JSONtoSend = new JSONObject();
        try {
            JSONtoSend.put("token", token);
        } catch (JSONException e) {
            Log.e(TAG, "onClick: exn " + e.getMessage());
            callback.onFail("Ошибка вложения токена" + e.getMessage());
            return;
        }
        try {
            JSONArray JsonProducts = new JSONArray();
            for (Product p : products) {
                JSONObject product = new JSONObject();
                product.put("id", p.id);
                product.put("count", 1);
                JsonProducts.put(product);
            }
            JSONObject pizzaOrInfo = new JSONObject();
            pizzaOrInfo.put("name", pizzaName);
            pizzaOrInfo.put("struct", JsonProducts);
            pizzaOrInfo.put("cost", cost);
            JSONtoSend.put("pizza", pizzaOrInfo);
        } catch (JSONException e) {
            Log.e(TAG, "onClick: exn " + e.getMessage());
            callback.onFail("Ошибка вложения состава" + e.getMessage());
            return;
        }
        Log.d(TAG, "apiAddPizza: " + JSONtoSend.toString());
        getterJSON.execute(host + UrlAddPizza, JSONtoSend.toString());
    }

    public static void apiAddProduct(String token, Product product, final ApiInterface.onCompleteWithResult callback) {
        final GetterJSON getterJSON = new GetterJSON(new GetterJSON.onCompleteEventHandler() {
            @Override
            public void onComplete(String Json) {
                if (Json == null) {
                    callback.onFail("Internet Error");
                    return;
                }
                Log.d(TAG, Json);
                try {
                    JSONObject result = new JSONObject(Json);
                    if (result.getInt("success") == 1) {
                        int id = result.getInt("id");
                        callback.onSuccess(String.valueOf(id));
                    } else {
                        callback.onFail(formatError(result.getJSONObject("error")));
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "onFail: " + e.getMessage());
                    callback.onFail(e.getMessage());
                }
            }

            @Override
            public void onFail(String error) {
                callback.onFail(error);
            }
        });
        JSONObject JSONtoSend = new JSONObject();
        try {
            JSONtoSend.put("token", token);
        } catch (JSONException e) {
            Log.e(TAG, "onClick: exn " + e.getMessage());
            callback.onFail("Ошибка вложения токена" + e.getMessage());
            return;
        }
        try {
            JSONObject jsonProduct = new JSONObject();
            jsonProduct.put("name", product.name);
            jsonProduct.put("unit", product.unit);
            jsonProduct.put("cost", product.cost);

            JSONtoSend.put("product", jsonProduct);
        } catch (JSONException e) {
            Log.e(TAG, "onClick: exn " + e.getMessage());
            callback.onFail("Ошибка вложения продукта" + e.getMessage());
            return;
        }
        Log.d(TAG, "apiAddProduct: " + JSONtoSend.toString());
        getterJSON.execute(host + UrlAddProduct, JSONtoSend.toString());
    }

    public static void apiRemoveProductFromPizza(String token, int pizzaId, int productId, final ApiInterface.onComplete callback) {
        GetterJSON getter = new GetterJSON(null, null, new GetterJSON.onCompleteEventHandler() {
            @Override
            public void onComplete(String Json) {
                if (Json == null) {
                    callback.onFail("Internet Error");
                    return;
                }
                try {
                    JSONObject result = new JSONObject(Json);
                    if (result.getInt("success") == 1) {
                        callback.onSuccess();
                    } else {
                        callback.onFail(formatError(result.getJSONObject("error")));
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "onCompleteWithResult: " + e.getMessage());
                    callback.onFail(e.getMessage());
                }
            }

            @Override
            public void onFail(String error) {
                callback.onFail(error);
            }
        });
        JSONObject JSONtoSend = new JSONObject();
        try {
            JSONtoSend.put("token", token);
        } catch (JSONException e) {
            Log.e(TAG, "apiGetPizzaStructure: exp " + e.getMessage());
            callback.onFail("Ошибка вложения токена");
            return;
        }
        try {
            JSONObject product = new JSONObject();

            product.put("pizza_id", pizzaId);
            product.put("product_id", productId);

            JSONtoSend.put("product", product);
        } catch (JSONException e) {
            Log.d(TAG, "apiGetPizzaStructure: " + e.getMessage());
            callback.onFail("Ошибка вложения продукта");
            return;
        }
        getter.execute(host + UrlRemoveProductFromPizza, JSONtoSend.toString());
    }

    public static void apiRemoveProduct(String token, int productId, final ApiInterface.onComplete callback) {
        GetterJSON getter = new GetterJSON(null, null, new GetterJSON.onCompleteEventHandler() {
            @Override
            public void onComplete(String Json) {
                if (Json == null) {
                    callback.onFail("Internet Error");
                    return;
                }
                try {
                    JSONObject result = new JSONObject(Json);
                    if (result.getInt("success") == 1) {
                        callback.onSuccess();
                    } else {
                        callback.onFail(formatError(result.getJSONObject("error")));
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "onCompleteWithResult: " + e.getMessage());
                    callback.onFail(e.getMessage());
                }
            }

            @Override
            public void onFail(String error) {
                callback.onFail(error);
            }
        });
        JSONObject JSONtoSend = new JSONObject();
        try {
            JSONtoSend.put("token", token);
        } catch (JSONException e) {
            Log.e(TAG, "apiGetPizzaStructure: exp " + e.getMessage());
            callback.onFail("Ошибка вложения токена");
            return;
        }
        try {
            JSONObject product = new JSONObject();
            product.put("product_id", productId);
            JSONtoSend.put("product", product);
        } catch (JSONException e) {
            Log.d(TAG, "apiGetPizzaStructure: " + e.getMessage());
            callback.onFail("Ошибка вложения продукта");
            return;
        }
        Log.d(TAG, "apiRemoveProduct: " + JSONtoSend.toString());
        getter.execute(host + UrlRemoveProduct, JSONtoSend.toString());
    }

    private static String formatError(JSONObject error) throws JSONException {
        String message = "";
        message = message + "code: " + error.getInt("code") + "\n";
        message = message + "msg: " + error.getString("message");
        return message;
    }


    public static class ApiInterface {
        private interface onFail {
            void onFail(String error);
        }

        public interface onCompleteWithResult extends onFail {
            void onSuccess(String result);
        }

        public interface onComplete extends onFail {
            void onSuccess();
        }

        public interface onCompleteGetUser extends onFail {
            void onSuccess(User u);
        }

        public interface onCompleteGetPizza extends onFail {
            void onSuccess(ArrayList<Pizza> p);
        }

        public interface onCompleteGetProductList extends onFail {
            void onSuccess(ArrayList<Product> products);
        }
    }
}
