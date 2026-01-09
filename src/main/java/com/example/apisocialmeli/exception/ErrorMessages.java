package com.example.apisocialmeli.exception;

public final class ErrorMessages {

    private ErrorMessages() {
    }

    public static final String INVALID_DATE_ORDER = "Parâmetro 'order' inválido. Use date_asc ou date_desc.";
    public static final String INVALID_NAME_ORDER = "Parâmetro 'order' inválido. Use name_asc ou name_desc.";

    public static final String ID_POSITIVE = "id deve ser maior que zero";
    public static final String USER_ID_POSITIVE = "O id do usuário deve ser maior que zero.";
    public static final String POST_ID_POSITIVE = "post_id deve ser maior que zero";
    public static final String PRODUCT_ID_POSITIVE = "product_id deve ser maior que zero";
    public static final String USER_ID_POSITIVE_PATH = "userId deve ser maior que zero";
    public static final String USER_ID_TO_FOLLOW_POSITIVE = "userIdToFollow deve ser maior que zero";
    public static final String USER_ID_TO_UNFOLLOW_POSITIVE = "userIdToUnfollow deve ser maior que zero";
    public static final String CATEGORY_POSITIVE = "category deve ser maior que zero";

    public static final String NAME_NOT_BLANK = "O nome é obrigatório";
    public static final String NAME_SIZE = "O nome não pode exceder 15 caracteres";
    public static final String NAME_PATTERN = "O nome não pode conter caracteres especiais";
    public static final String EMAIL_NOT_BLANK = "O e-mail é obrigatório";
    public static final String EMAIL_VALID = "O e-mail deve ser válido";
    public static final String PASSWORD_NOT_BLANK = "A senha é obrigatória";
    public static final String PASSWORD_SIZE = "A senha deve ter entre 6 e 20 caracteres";

    public static final String UPDATE_NAME_NOT_BLANK = "O nome não pode estar vazio";
    public static final String UPDATE_NAME_SIZE = "O nome não pode exceder 40 caracteres";
    public static final String UPDATE_EMAIL_NOT_BLANK = "O e-mail não pode estar vazio";
    public static final String UPDATE_EMAIL_VALID = "O e-mail deve ser válido";
    public static final String UPDATE_PASSWORD_NOT_BLANK = "A senha não pode estar vazia";
    public static final String UPDATE_PASSWORD_SIZE = "A senha deve ter pelo menos 6 caracteres";

    public static final String DATE_NOT_BLANK = "A data não pode estar vazia";
    public static final String DATE_PATTERN = "A data deve estar no formato dd-MM-aaaa";
    public static final String PRODUCT_NOT_NULL = "O produto não pode ser nulo";
    public static final String PRICE_MIN = "O preço deve ser maior que zero";
    public static final String PRICE_MAX = "O preço não pode exceder 10.000.000";
    public static final String HAS_PROMO_NOT_NULL = "has_promo não pode ser nulo";
    public static final String DISCOUNT_MIN = "O desconto deve ser maior que zero";
    public static final String DISCOUNT_MAX = "O desconto não pode ser maior que 1.0";

    public static final String PRODUCT_NAME_NOT_BLANK = "product_name não pode estar vazio";
    public static final String PRODUCT_NAME_SIZE = "product_name não pode exceder 40 caracteres";
    public static final String PRODUCT_NAME_PATTERN = "product_name não pode conter caracteres especiais";
    public static final String TYPE_NOT_BLANK = "type não pode estar vazio";
    public static final String TYPE_SIZE = "type não pode exceder 15 caracteres";
    public static final String TYPE_PATTERN = "type não pode conter caracteres especiais";
    public static final String BRAND_NOT_BLANK = "brand não pode estar vazio";
    public static final String BRAND_SIZE = "brand não pode exceder 25 caracteres";
    public static final String BRAND_PATTERN = "brand não pode conter caracteres especiais";
    public static final String COLOR_NOT_BLANK = "color não pode estar vazio";
    public static final String COLOR_SIZE = "color não pode exceder 15 caracteres";
    public static final String COLOR_PATTERN = "color não pode conter caracteres especiais";
    public static final String NOTES_SIZE = "notes não pode exceder 80 caracteres";
    public static final String NOTES_PATTERN = "notes não pode conter caracteres especiais";

    public static final String USER_EXISTS_ID = "Já existe um usuário com o id %d";
    public static final String USER_NOT_FOUND = "Usuário não encontrado: %d";
    public static final String SELF_FOLLOW = "Um usuário não pode seguir a si mesmo.";
    public static final String ALREADY_FOLLOWING = "O usuário %d já segue %d.";
    public static final String SELF_UNFOLLOW = "Um usuário não pode deixar de seguir a si mesmo.";
    public static final String NOT_FOLLOWING = "O usuário %d não segue %d.";

    public static final String PROMO_REQUIRES_DISCOUNT = "Produtos em promoção devem incluir o campo discount.";
    public static final String DISCOUNT_ONLY_WITH_PROMO = "Discount só pode ser informado quando has_promo = true.";
}
