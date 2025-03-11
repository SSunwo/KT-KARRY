# import xgboost as xgb
# import pandas as pd
# import pickle as pickle

# from sklearn.model_selection import train_test_split
# from sklearn.metrics import mean_absolute_error


# # ✅ 1. 데이터 로드
# df = pd.read_csv("fare_data.csv")  # 요금 데이터 로드

# # ✅ 2. X, Y 분리
# X = df[['distance', 'weight', 'size', 'toll_fare', 'fuel_price']]
# y = df['calculated_price']

# # ✅ 3. 학습/테스트 데이터 분할
# X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=42)

# # ✅ 4. XGBoost 모델 학습
# model = xgb.XGBRegressor(n_estimators=100, learning_rate=0.1, max_depth=5)
# model.fit(X_train, y_train)

# # ✅ 5. 모델 평가
# y_pred = model.predict(X_test)
# mae = mean_absolute_error(y_test, y_pred)
# print(f"✅ 모델 평가 완료: MAE = {mae:.2f}")

# # ✅ 6. 모델 저장
# with open("fare_prediction_xgb.pkl", "wb") as file:
#     pickle.dump(model, file)

# print("✅ 모델 저장 완료: fare_prediction_xgb.pkl")
##########################################################################################
# # 데이터 샘플링: 30% 데이터만 사용하여 학습
# df_sampled = df.sample(frac=0.3, random_state=42)

# # 샘플링된 데이터로 X, Y 분리
# X_sampled = df_sampled[features]
# y_sampled = df_sampled[target]

# # 학습/테스트 데이터 분할
# X_train, X_test, y_train, y_test = train_test_split(X_sampled, y_sampled, test_size=0.2, random_state=42)

# # XGBoost 모델 학습 속도 개선 버전
# model = xgb.XGBRegressor(
#     n_estimators=30,          # 트리 개수 줄이기 (50 -> 30)
#     learning_rate=0.1,        # 학습률 조정
#     max_depth=3,              # 트리 깊이 더 줄이기
#     subsample=0.7,            # 샘플링 비율 적용
#     random_state=42
# )

# # 모델 학습
# model.fit(X_train, y_train)

# # 모델 평가
# y_pred = model.predict(X_test)
# mae = mean_absolute_error(y_test, y_pred)

# # 모델 저장
# model_path = "/mnt/data/fare_prediction_xgb.pkl"
# with open(model_path, "wb") as file:
#     import pickle
#     pickle.dump(model, file)

# # 평가 결과 출력
# mae, model_path