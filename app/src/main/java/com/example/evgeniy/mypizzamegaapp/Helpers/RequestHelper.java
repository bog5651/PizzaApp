package com.example.evgeniy.mypizzamegaapp.Helpers;

import android.util.Log;

import com.example.evgeniy.mypizzamegaapp.Models.Pizza;
import com.example.evgeniy.mypizzamegaapp.Models.User;
import com.example.evgeniy.mypizzamegaapp.Tasks.GetterJSON;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RequestHelper {

    private static final String TAG = "requestHelper";

    private static final String host = "http://192.168.0.62";
    private static final String UrlLogin = "/api/login";
    private static final String UrlUser = "/api/user";
    private static final String UrlPizza = "/api/pizza";
    private static final String UrlRegister = "/api/register";

    public static void apiLogin(String login, String password, final ApiInterface.onComplete callback) {
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
                        String token = result.getJSONObject("data").getString("token");
                        callback.onSuccess(token);
                    } else {
                        callback.onFail(formatError(result.getJSONObject("error")));
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "onComplete: exn " + e.getMessage());
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
                        callback.onSuccess(user);
                    } else {
                        callback.onFail(formatError(result.getJSONObject("error")));
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "onComplete: " + e.getMessage());
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
                        JSONArray JSONPizzas = result.getJSONArray("pizzas");
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
                    Log.e(TAG, "onComplete: " + e.getMessage());
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

    public static void apiRegister(String login, String password, String firstName, String secondName, final ApiInterface.onComplete callback) {
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
                    Log.e(TAG, "onComplete: " + e.getMessage());
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

        public interface onComplete extends onFail {
            void onSuccess(String result);
        }

        public interface onCompleteGetUser extends onFail {
            void onSuccess(User u);
        }

        public interface onCompleteGetPizza extends onFail {
            void onSuccess(ArrayList<Pizza> p);
        }
    }
}
