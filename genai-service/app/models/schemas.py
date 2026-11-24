from pydantic import BaseModel, Field
from typing import Optional


class SymptomRequest(BaseModel):
    symptoms: list[str] = Field(..., min_length=1, example=["fever", "headache", "fatigue"])
    age: int = Field(..., gt=0, lt=120, example=28)
    gender: str = Field(..., example="Male")
    duration_days: Optional[int] = Field(default=None, example=3)


class ConditionResult(BaseModel):
    condition: str
    urgency: str          # Low | Medium | High
    description: str
    recommended_specialist: str


class SymptomResponse(BaseModel):
    patient_profile: str
    possible_conditions: list[ConditionResult]
    general_advice: str
    disclaimer: str
