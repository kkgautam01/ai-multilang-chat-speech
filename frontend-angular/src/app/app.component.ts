import { Component, ElementRef, ViewChild } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { marked } from 'marked';
import { DomSanitizer, SafeHtml } from '@angular/platform-browser';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './app.component.html',
})
export class AppComponent {
  provider = 'ollama';
  prompt = '';
  loading = false;

  messages: { role: 'user' | 'ai'; text: string | SafeHtml; isHtml?: boolean }[] = [];

  @ViewChild('scrollContainer') scrollContainer!: ElementRef;

  constructor(private http: HttpClient, private sanitizer: DomSanitizer) {}

  send() {
    if (!this.prompt.trim()) return;
  
    const userMessage = this.prompt;
    const apiUrl = 'http://localhost:8080/v1/chat';
    const body = { prompt: userMessage, provider: this.provider };
  
    this.messages.push({ role: 'user', text: userMessage, isHtml: false });
    this.prompt = '';
    this.loading = true;
    this.scrollToBottom();
  
    const aiIndex = this.messages.length;
    this.messages.push({ role: 'ai', text: '', isHtml: true });
  
    this.http.post<{ reply: string }>(apiUrl, body).subscribe({
      next: (res) => {
        const reply = res?.reply || '';
        const lines = reply.split('\n');
  
        const streamToken = (i = 0) => {
          if (i >= lines.length) {
            this.loading = false;
            this.scrollToBottom();
            return;
          }
  
          try {
            const parsed = JSON.parse(lines[i]);
  
            const token = parsed.response || '';
            const decoded = this.decodeUnicodeSafe(token);
            const rawHtml = marked.parseInline(decoded);
  
            const current = this.messages[aiIndex].text as string;
            this.messages[aiIndex].text = (current + rawHtml).trim();
  
            this.scrollToBottom();
            setTimeout(() => streamToken(i + 1), 50);
          } catch (e) {
            console.warn('Bad line:', lines[i]);
            streamToken(i + 1);
          }
        };
  
        streamToken();
      },
      error: (err) => {
        this.messages[aiIndex].text = 'Error: ' + (err?.message || 'Unknown error');
        this.loading = false;
        this.scrollToBottom();
      }
    });
  }
  
  // Utility method: decode special characters
  decodeUnicodeSafe(text: string): string {
    try {
      // Handles emojis and special characters safely
      return decodeURIComponent(escape(text));
    } catch (e) {
      return text;
    }
  }

  scrollToBottom() {
    setTimeout(() => {
      const el = this.scrollContainer?.nativeElement;
      if (el) el.scrollTop = el.scrollHeight;
    }, 100);
  }
}
