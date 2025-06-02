import { NextResponse } from "next/server";
import type { NextRequest } from "next/server";

export function middleware(request: NextRequest) {
    const isLoggedIn = request.cookies.get("JSESSIONID");

    const protectedPaths = ["/student", "/lecturer", "/admin"];
    const path = request.nextUrl.pathname;
    const isProtected = protectedPaths.some(p => path.startsWith(p));

    if (isProtected && !isLoggedIn) {
        return NextResponse.redirect(new URL("/login", request.url));
    }

    return NextResponse.next();
}