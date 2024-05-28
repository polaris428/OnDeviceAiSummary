from transformers import AutoModelForSeq2SeqLM, AutoTokenizer
import os
import torch

model = None
tokenizer = None

def load_model():
    global model
    if model is None:
        model_dir = os.path.join(os.path.dirname(__file__), 'model')
        model = AutoModelForSeq2SeqLM.from_pretrained(model_dir)
    return model

def load_tokenizer():
    global tokenizer
    if tokenizer is None:
        model_dir = os.path.join(os.path.dirname(__file__), 'model')
        tokenizer = AutoTokenizer.from_pretrained(model_dir)
    return tokenizer

def summarize(text):
    global model, tokenizer
    if model is None or tokenizer is None:
        load_model()
        load_tokenizer()

    inputs = tokenizer(text, return_tensors="pt")
    input_ids = inputs['input_ids']
    attention_mask = inputs['attention_mask']

    outputs = model.generate(input_ids=input_ids, attention_mask=attention_mask)
    summary = tokenizer.decode(outputs[0], skip_special_tokens=True)

    # 메모리 해제
    del inputs
    del input_ids
    del attention_mask
    del outputs
    torch.cuda.empty_cache()

    return summary

def cleanup():
    global model, tokenizer
    del model
    del tokenizer
    torch.cuda.empty_cache()

if __name__ == "__main__":
    print(summarize("코틀린 멀티 플랫폼은 모바일 뿐만 아니라 JVM, Native 및 JS 타깃을 지원하는 넓은 개념의 기술로 안드로이드 네이티브 앱 개발자들에게는 iOS 개발을 할 수 있는 사실에 큰 주목을 받게 됐다."))
