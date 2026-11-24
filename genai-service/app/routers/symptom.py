from fastapi import APIRouter, HTTPException
from app.models.schemas import SymptomRequest, SymptomResponse
from app.services.llm_service import analyze_symptoms
import json

router = APIRouter()


@router.post("/symptom-check", response_model=SymptomResponse)
async def symptom_check(req: SymptomRequest):
    """
    AI-powered symptom checker.
    Accepts patient symptoms and returns possible conditions with urgency levels.
    """
    try:
        return await analyze_symptoms(
            symptoms=req.symptoms,
            age=req.age,
            gender=req.gender,
            duration_days=req.duration_days,
        )
    except json.JSONDecodeError:
        raise HTTPException(status_code=502, detail="AI service returned malformed response. Please retry.")
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"GenAI service error: {str(e)}")
