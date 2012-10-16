(ns friend-form-login.workflow
  (:require [cemerick.friend :as friend]
            [cemerick.friend.util :as util]))

(defn make-auth [identity]
  (with-meta identity
    {:type ::friend/auth
     ::friend/workflow :email-login
     ::friend/redirect-on-auth? true}))

(defn email-workflow
  [& {:keys [credential-fn login-uri] :as config}]
  (fn [{:keys [uri request-method] :as request}]
    (when (and (= uri (util/gets :login-uri config
                                 (::friend/auth-config request)))
               (= :post request-method))
      ;; Get the credential function from either what was passed in
      ;; or what we have stored in the request via Friend.
      (if-let [creds ((util/gets :credential-fn
                                 config
                                 (::friend/auth-config request)))]
        (make-auth creds)))))