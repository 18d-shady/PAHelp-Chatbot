# Example: rasa_model.py

import rasa
from rasa.core.agent import Agent
from rasa.core.interpreter import RasaNLUInterpreter

def load_agent(model_path):
    # Load your trained Rasa model
    agent = Agent.load(model_path)
    return agent

def get_response(agent, message):
    # Get a response from the Rasa model
    response = agent.handle_text(message)
    return response[0]["text"]
