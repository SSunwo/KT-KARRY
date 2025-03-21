# 🚛 KARRY: AI 기반 물류 매칭 플랫폼

## 📌 프로젝트 개요

KARRY는 화주(Shipper)와 차주(Carrier) 간의 원활한 협력을 지원하는 **AI 기반 물류 매칭 플랫폼**입니다.  
배차 최적화, 요금 예측, 데이터 정합성 향상 등을 목표로 하며, AI와 데이터 아키텍처 최적화를 통해 효율적인 물류 서비스를 제공합니다.

<p align="center">
  <img width="700" alt="image" src="https://github.com/user-attachments/assets/f0e41292-d9ba-47f6-bc77-65f022dab6f0" />
</p>

---

## 🛠 기술 스택

### **Frontend**
- Vue.js 3.5.13
- Vuex (상태 관리)
- TailwindCSS + Flowbite (UI 디자인)

### **Backend**
- Spring Boot 3.3.9 (JPA)
- PostgreSQL (Database)
- FastAPI (AI 모델 배포)
- OpenAI API (배차 추천)

### **AI 적용 모델**
- **XGBoost** (운송 요금 예측)
- **OpenAI GPT API** (배차 추천)

### **기타 사항**
- 로컬 실행
- API 테스트: Postman
- 사용 API : 다음 우편번호 API, 카카오 로컬 API, 네이버 지도 API

<p align="center">
  <img width="700" alt="image" src="https://github.com/user-attachments/assets/d8ae7fc3-e1bd-4cc3-93ed-0561da8447ab" />
</p>

---

## 🔑 주요 기능

### 1️⃣ **AI 기반 물류 매칭 시스템**
- 기존 단순 거리 기반 매칭에서 벗어나, **차주의 운송 패턴 및 신뢰도**를 분석하여 최적 배차 추천
- OpenAI API를 활용하여 **운송 요청에 맞는 최적의 차주 추천**
- 배차 속도 증가 및 효율적인 운송 프로세스 구축

<p align="center">
  <img width="700" alt="image" src="https://github.com/user-attachments/assets/fe98e526-1bc6-43e0-be04-f79854565534" />
</p>

### 2️⃣ **AI 기반 요금 예측 (XGBoost)**
- 기존 정적인 요금 모델을 개선하여 **거리, 무게, 크기, 연료비, 톨게이트 비용** 등의 요소를 고려한 **동적 요금 산정**
- **XGBoost 회귀 모델**을 통해 정확한 운송 요금 예측 제공

### 3️⃣ **DAG 기반 Fact Table (데이터 정합성 최적화)**
- **실시간 대량 트랜잭션 최적화**를 위한 **DAG(Directed Acyclic Graph) 기반 Fact Table** 적용
- 실시간 운송 현황 추적 및 **Batch 업데이트 대시보드** 구성

<p align="center">
  <img width="700" alt="image" src="https://github.com/user-attachments/assets/fcb1c86f-13c4-4259-b6ea-df1386e9ba49" />
</p>




### 4️⃣ **배송 현황 관리 및 정산 시스템**
- 차주는 **운송 완료 즉시 정산** 가능 → **현금 흐름 개선**
- Dashboard에서 **운송 및 정산 상태** 실시간 확인 가능

### 5️⃣ **Vue.js 기반 반응형 UI**
- 차주(Carrier)와 화주(Shipper)의 역할에 따라 UI가 동적으로 변경
- 차주는 배차 수락 및 운송 진행, 화주는 운송 요청 및 대금 정산을 확인 가능

---

## 🏆 팀 및 기여자
- Backend 개발: 김선우
- Frontend 개발: 김선우
- AI 모델링: 김선우
- DB 설계: 김선우

---

## 📥 설치 및 실행 방법

### Project setup
<pre><code>npm install</code></pre>

### Compiles and hot-reloads for development
<pre><code>npm run serve</code></pre>

### Compiles and minifies for production
<pre><code>npm run build</code></pre>

### Lints and fixes files
<pre><code>npm run lint</code></pre>
