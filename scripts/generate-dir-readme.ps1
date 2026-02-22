<#
generate-dir-readme.ps1
루트 폴더의 최상위 디렉토리를 스캔해 Markdown 형식의 "디렉토리 인덱스" 섹션을 생성하거나 기존 README의 해당 섹션을 업데이트합니다.
Windows PowerShell v5.1 용 (사용법 주석 포함)

기능 요약:
 - 최상위 디렉토리(파일 제외)를 나열하고 상대 링크를 생성
 - 기본 제외 패턴(.git, build, .idea 등)을 적용
 - 기존 README에 "## 디렉토리 인덱스" 섹션이 있으면 항목을 병합하거나(기본) 덮어씀
 - -PreserveExisting 플래그가 있으면 이미 존재하는 항목은 보존하고 없는 항목만 추가
 - -DryRun 모드로 결과를 콘솔에 출력만 함

사용 예:
 PS> .\scripts\generate-dir-readme.ps1 -RootPath . -OutReadme README.md -DryRun
 PS> .\scripts\generate-dir-readme.ps1 -PreserveExisting -OutReadme README.md

#>
[CmdletBinding()]
param(
    [string]
    $RootPath = '.',

    [string]
    $OutReadme = 'README.md',

    [string]
    $InsertSectionHeader = '## 디렉토리 인덱스',

    [string[]]
    $ExcludePatterns = @('.git', 'build', 'out', 'node_modules', '.idea', 'target', 'gradle', 'gradle\wrapper', 'build.gradle', 'gradlew', 'gradlew.bat'),

    [switch]
    $PreserveExisting,

    [switch]
    $DryRun,

    [switch]
    $Force
)

function ShouldExclude($name, $patterns) {
    foreach ($p in $patterns) {
        if ($name -ieq $p) { return $true }
        if ($name -like $p) { return $true }
    }
    return $false
}

Push-Location -Path $RootPath
try {
    # 최상위 디렉토리만 취득
    $items = Get-ChildItem -Force -LiteralPath . | Where-Object { $_.PSIsContainer -eq $true } | Sort-Object Name
    $dirs = @()
    foreach ($it in $items) {
        if (ShouldExclude $it.Name $ExcludePatterns) { continue }
        $dirs += $it
    }

    if ($dirs.Count -eq 0) {
        Write-Output "No directories found to index under $RootPath"
        return
    }

    # 마크다운 섹션 조립
    $sb = New-Object System.Text.StringBuilder
    $sb.AppendLine("$InsertSectionHeader") | Out-Null
    $sb.AppendLine('') | Out-Null
    $sb.AppendLine('아래는 저장소 최상위 디렉토리 목록과 간단한 설명 템플릿입니다. 각 항목을 적절히 수정해 주세요.') | Out-Null
    $sb.AppendLine('') | Out-Null

    # 목차(간략 목록)
    $sb.AppendLine('#### 목차') | Out-Null
    $sb.AppendLine('') | Out-Null
    foreach ($d in $dirs) {
        $link = "./$($d.Name)/"
        $sb.AppendLine("- [$($d.Name)/]($link) — (간단 설명을 여기에 작성)") | Out-Null
    }
    $sb.AppendLine('') | Out-Null

    # 상세 템플릿
    foreach ($d in $dirs) {
        $sb.AppendLine("### $($d.Name)/") | Out-Null
        $sb.AppendLine('') | Out-Null
        $sb.AppendLine('- 목적: (예: 라이브러리, 예제, 서비스 등 한 줄 설명)') | Out-Null
        $sb.AppendLine('- 주요 내용: (예: 주요 파일/하위폴더/모듈)') | Out-Null
        $sb.AppendLine('- 사용법 / 빌드: (예: gradlew build, javac 등)') | Out-Null
        $sb.AppendLine('- 담당자 / 참고: (예: GitHub ID, OWNER 파일 위치)') | Out-Null
        $sb.AppendLine('- 비고:') | Out-Null
        $sb.AppendLine('') | Out-Null
    }

    $newSection = $sb.ToString()

    if (-not (Test-Path -LiteralPath $OutReadme)) {
        if ($DryRun) {
            Write-Output "[DryRun] README 파일이 존재하지 않아 새 파일을 생성합니다: $OutReadme"
            Write-Output $newSection
            return
        } else {
            Set-Content -LiteralPath $OutReadme -Value $newSection -Encoding UTF8
            Write-Output "Created $OutReadme with directory index."
            return
        }
    }

    # 기존 README 내용 로드
    $content = Get-Content -LiteralPath $OutReadme -Raw -ErrorAction Stop

    $headerEscaped = [regex]::Escape($InsertSectionHeader)
    $pattern = "$headerEscaped.*(?=(^##\s)|\z)"  # 헤더부터 다음 '## ' 또는 파일 끝까지 매칭 (singleline)

    $existingSection = [regex]::Match($content, $pattern, [System.Text.RegularExpressions.RegexOptions]::Singleline)

    if ($existingSection.Success) {
        if ($PreserveExisting) {
            # 기존 섹션의 항목 목록을 추출하여 새 디렉토리만 추가
            $existingText = $existingSection.Value
            $existingNames = @()
            $existingText -split "`n" | ForEach-Object {
                if ($_ -match '^- \[(.+?)/\]') { $existingNames += $matches[1] }
            }
            $toAdd = @()
            foreach ($d in $dirs) {
                if ($existingNames -notcontains $d.Name) { $toAdd += $d }
            }
            if ($toAdd.Count -eq 0) {
                Write-Output "No new directories to add. README unchanged."
                return
            }
            # 추가할 새 섹션 텍스트 생성(간단 목차 + 상세)
            $addSb = New-Object System.Text.StringBuilder
            foreach ($d in $toAdd) {
                $link = "./$($d.Name)/"
                $addSb.AppendLine("- [$($d.Name)/]($link) — (간단 설명을 여기에 작성)") | Out-Null
            }
            $addSb.AppendLine('') | Out-Null
            foreach ($d in $toAdd) {
                $addSb.AppendLine("### $($d.Name)/") | Out-Null
                $addSb.AppendLine('') | Out-Null
                $addSb.AppendLine('- 목적: (예: 라이브러리, 예제, 서비스 등 한 줄 설명)') | Out-Null
                $addSb.AppendLine('- 주요 내용: (예: 주요 파일/하위폴더/모듈)') | Out-Null
                $addSb.AppendLine('- 사용법 / 빌드: (예: gradlew build, javac 등)') | Out-Null
                $addSb.AppendLine('- 담당자 / 참고: (예: GitHub ID, OWNER 파일 위치)') | Out-Null
                $addSb.AppendLine('- 비고:') | Out-Null
                $addSb.AppendLine('') | Out-Null
            }

            $updatedSection = $existingText + "`n" + $addSb.ToString()
            $newContent = $content.Substring(0, $existingSection.Index) + $updatedSection + $content.Substring($existingSection.Index + $existingSection.Length)

            if ($DryRun) {
                Write-Output "[DryRun] --- New content for existing README section ---"
                Write-Output $updatedSection
                return
            }
            Set-Content -LiteralPath $OutReadme -Value $newContent -Encoding UTF8
            Write-Output "README updated: added $($toAdd.Count) new directories to existing section."
            return
        } else {
            # 덮어쓰기 모드: 기존 섹션을 새 섹션으로 대체
            $newContent = $content.Substring(0, $existingSection.Index) + $newSection + $content.Substring($existingSection.Index + $existingSection.Length)
            if ($DryRun) {
                Write-Output "[DryRun] --- New section that would replace existing one ---"
                Write-Output $newSection
                return
            }
            Set-Content -LiteralPath $OutReadme -Value $newContent -Encoding UTF8
            Write-Output "README section replaced with updated directory index."
            return
        }
    } else {
        # 기존 섹션 없음: 파일 끝에 추가
        if ($DryRun) {
            Write-Output "[DryRun] --- New section to append to README ---"
            Write-Output $newSection
            return
        }
        Add-Content -LiteralPath $OutReadme -Value "`n" -Encoding UTF8
        Add-Content -LiteralPath $OutReadme -Value $newSection -Encoding UTF8
        Write-Output "Appended new directory index section to README."
        return
    }
}
catch {
    Write-Error "Error: $_"
    exit 1
}
finally {
    Pop-Location
}

