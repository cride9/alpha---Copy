"""Collect existing, completed MK2 runs. Does not execute tests or alter their outcome."""
from pathlib import Path
from collections import Counter
import hashlib
import json
import re
import shutil
import xml.etree.ElementTree as ET
from datetime import datetime, timezone

ROOT = Path(__file__).resolve().parents[1]
APP = ROOT / 'software/compose-tetris/app'
OUT = ROOT / 'reports'

def sha256(path):
    return hashlib.sha256(path.read_bytes()).hexdigest()

def collect():
    junit = sorted((APP / 'build/test-results/testDebugUnitTest').glob('TEST-*.xml'))
    if not junit:
        raise SystemExit('No JUnit results; first run :app:testDebugUnitTest.')
    suites = [ET.parse(p).getroot() for p in junit]
    lint_path = APP / 'build/reports/lint-results-debug.xml'
    detekt_path = OUT / 'static/detekt.xml'
    lint = ET.parse(lint_path).getroot()
    detekt = ET.parse(detekt_path).getroot()
    (OUT / 'unit/xml').mkdir(parents=True, exist_ok=True)
    (OUT / 'static').mkdir(parents=True, exist_ok=True)
    for p in junit:
        shutil.copy2(p, OUT / 'unit/xml' / p.name)
    html = APP / 'build/reports/tests/testDebugUnitTest'
    shutil.copytree(html, OUT / 'unit/html', dirs_exist_ok=True)
    for p in (APP / 'build/reports').glob('lint-results-debug.*'):
        shutil.copy2(p, OUT / 'static' / p.name)
    apk = APP / 'build/outputs/apk/debug/app-debug.apk'
    ledger = ROOT / 'docs/Compose_Tetris_Funkcionalis_Jegyzokonyv.md'
    statuses = re.findall(r'^\| F-\d{3} \| (PASS|FAIL|BLOCKED|NOT RUN) \|',
                          ledger.read_text(encoding='utf-8') if ledger.exists() else '', re.M)
    manual = dict(Counter(statuses))
    result = {
        'collected_at_utc': datetime.now(timezone.utc).isoformat(),
        'upstream_commit': '234416c455cd0b5524b7f2a7e91aaa9f6206457a',
        'junit': {k: sum(int(s.get(k, 0)) for s in suites)
                  for k in ('tests', 'failures', 'errors', 'skipped')},
        'junit_suites': [{'name': s.get('name'), 'tests': int(s.get('tests')),
                          'timestamp_utc': s.get('timestamp')} for s in suites],
        'lint': {'total': len(lint.findall('issue')),
                 'severity': dict(Counter(i.get('severity') for i in lint)),
                 'rules': dict(Counter(i.get('id') for i in lint))},
        'detekt': {'total': len(detekt.findall('.//error')),
                   'rules': dict(Counter(i.get('source').split('.')[-1]
                                         for i in detekt.findall('.//error')))},
        'manual_android': {'ledger': 'docs/Compose_Tetris_Funkcionalis_Jegyzokonyv.md',
                           'recorded_statuses': manual,
                           'note': 'Counts from the manually maintained ledger; not verified by this script.'},
        'apk': {'path': 'software/compose-tetris/app/build/outputs/apk/debug/app-debug.apk',
                'sha256': sha256(apk) if apk.exists() else None},
    }
    (OUT / 'run-summary.json').write_text(json.dumps(result, indent=2, ensure_ascii=False)+'\n', encoding='utf-8')
    print(json.dumps(result, indent=2, ensure_ascii=False))

if __name__ == '__main__':
    collect()
