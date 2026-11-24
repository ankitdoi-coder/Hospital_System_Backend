import os
import json
from langchain_groq import ChatGroq
from langchain.prompts import ChatPromptTemplate
from app.models.schemas import SymptomResponse, ConditionResult

llm = ChatGroq(
    model="llama-3.1-8b-instant",
    api_key=os.getenv("GROQ_API_KEY"),
    temperature=0.3,
)

SYMPTOM_PROMPT = ChatPromptTemplate.from_template(
    """You are a medical AI assistant. Analyze the following patient information and return a JSON response.

Patient Info:
- Age: {age}
- Gender: {gender}
- Symptoms: {symptoms}
- Duration: {duration}

Return ONLY valid JSON in this exact format (no markdown, no extra text):
{{
  "patient_profile": "brief one-line patient summary",
  "possible_conditions": [
    {{
      "condition": "Condition Name",
      "urgency": "Low|Medium|High",
      "description": "brief explanation in 1-2 sentences",
      "recommended_specialist": "Doctor specialization to consult"
    }}
  ],
  "general_advice": "2-3 actionable self-care tips",
  "disclaimer": "This is AI-generated information and not a substitute for professional medical advice."
}}

Provide exactly 3 possible conditions ordered by likelihood."""
)


async def analyze_symptoms(symptoms: list, age: int, gender: str, duration_days: int | None) -> SymptomResponse:
    duration_text = f"{duration_days} days" if duration_days else "not specified"

    chain = SYMPTOM_PROMPT | llm
    result = await chain.ainvoke({
        "age": age,
        "gender": gender,
        "symptoms": ", ".join(symptoms),
        "duration": duration_text,
    })

    data = json.loads(result.content)

    return SymptomResponse(
        patient_profile=data["patient_profile"],
        possible_conditions=[ConditionResult(**c) for c in data["possible_conditions"]],
        general_advice=data["general_advice"],
        disclaimer=data["disclaimer"],
    )
