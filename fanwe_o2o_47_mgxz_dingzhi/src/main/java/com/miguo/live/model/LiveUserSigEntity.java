package com.miguo.live.model;

import java.util.List;

/**
 * Created by didik on 2016/7/28.
 */
public class LiveUserSigEntity {

    /**
     * result : [{"body":[{"usersig":"eJxljl1PgzAYhe-5FYRbRdtCgZp4IRv7kBHFiduuSKFlaZYBKUVYFv
     * *7E5dI4nv7PO8556zpum68r9Z3NM*rtlSpOtXc0B90Axi3f7CuBUupSi3J
     * -kHe10LylBaKywFCjDECYOwIxkslCnE1OMeOi7hrZhi6pk08ZnqUQJNmtgctjIoMotF3ww7pMOE33r5kw0sBHitiP8AoSCbLeDJPpvw1gLk-jUJp9wsVx6fF883cOfp1-7abZVWIk5ftOom65X7l58kmr1jrWW0GNkEXhfcze0eLwik-SCO7aNs-VQcCwvxxVKnEkV8HOcTFiLhkRD*5bERVDgICEENkgZ8ztC-tG3KYY-E_"}]}]
     * message : 操作成功
     * token : 9254d5c9dccbd7558af664f3548ef9c9
     * statusCode : 200
     */

    private String message;
    private String token;
    private String statusCode;
    private List<ResultBean> result;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public List<ResultBean> getResult() {
        return result;
    }

    public void setResult(List<ResultBean> result) {
        this.result = result;
    }

    public static class ResultBean {
        /**
         * usersig : eJxljl1PgzAYhe-5FYRbRdtCgZp4IRv7kBHFiduuSKFlaZYBKUVYFv
         * *7E5dI4nv7PO8556zpum68r9Z3NM*rtlSpOtXc0B90Axi3f7CuBUupSi3J
         * -kHe10LylBaKywFCjDECYOwIxkslCnE1OMeOi7hrZhi6pk08ZnqUQJNmtgctjIoMotF3ww7pMOE33r5kw0sBHitiP8AoSCbLeDJPpvw1gLk-jUJp9wsVx6fF883cOfp1-7abZVWIk5ftOom65X7l58kmr1jrWW0GNkEXhfcze0eLwik-SCO7aNs-VQcCwvxxVKnEkV8HOcTFiLhkRD*5bERVDgICEENkgZ8ztC-tG3KYY-E_
         */

        private List<BodyBean> body;

        public List<BodyBean> getBody() {
            return body;
        }

        public void setBody(List<BodyBean> body) {
            this.body = body;
        }

        public static class BodyBean {
            private String usersig;

            public String getUsersig() {
                return usersig;
            }

            public void setUsersig(String usersig) {
                this.usersig = usersig;
            }
        }
    }
}
