import pickle
import pandas as pd
from fastapi import FastAPI


# 1. FastAPI 앱 생성
app = FastAPI()

# 2. 학습된 XGBoost 모델 로드
model_path = "fare_prediction_xgb.pkl"
with open(model_path, "rb") as file:
    model = pickle.load(file)

@app.post("/predict_fare/")
async def predict_fare(data: dict):
    """
    요금 예측 API
    - 입력: 거리(km), 무게(kg), 크기(CBM), 톨게이트 비용, 연료 비용
    - 출력: 예상 요금 (원)
    """
    # ✅ 3. 입력 데이터 변환
    input_data = pd.DataFrame([[
        data["distance"],
        data["weight"],
        data["size"],
        data["toll_fare"],
        data["fuel_price"]
    ]], columns=["distance", "weight", "size", "toll_fare", "fuel_price"])

    # ✅ 4. 모델 예측 수행
    predicted_price = model.predict(input_data)[0]

    return {"predicted_price": round(predicted_price, 2)}