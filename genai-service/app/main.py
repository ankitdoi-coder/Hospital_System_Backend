from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
from contextlib import asynccontextmanager
from app.routers import symptom
import os 


@asynccontextmanager
async def lifespan(app: FastAPI):
    print("🤖 GenAI Service starting up...")
    if not os.getenv("GROQ_API_KEY"):
        print("⚠️  WARNING: GROQ_API_KEY is not set!")
    yield
    print("🛑 GenAI Service shutting down...")


app = FastAPI(
    title="Healthcare GenAI Service",
    description="AI-powered features for Smart Healthcare System",
    version="1.0.0",
    lifespan=lifespan,
)

app.add_middleware(
    CORSMiddleware,
    allow_origins=["http://localhost:5173", "http://localhost:3000", "http://localhost:8080"],
    allow_methods=["*"],
    allow_headers=["*"],
)

app.include_router(symptom.router, prefix="/ai", tags=["Symptom AI"])


@app.get("/health")
def health_check():
    return {"status": "UP", "service": "genai-service", "port": 8090}
